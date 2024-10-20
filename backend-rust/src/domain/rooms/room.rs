use crate::domain::packets::{ClientPacket, ServerPacket};
use crate::domain::rooms::message::ChatMessage;
use crate::domain::rooms::session::MessageSession;
use crate::error::Error::BlankMessageError;
use anyhow::Result;
use jiff::Zoned;

use futures::future::join_all;
use std::collections::HashMap;

pub struct Room {
    pub name: String,
    pub sessions: HashMap<String, MessageSession>,
    pub messages: Vec<ChatMessage>,
    pub created_at: Zoned,
}

impl Room {
    pub fn new(name: String) -> Room {
        Room {
            name,
            sessions: HashMap::new(),
            messages: Vec::new(),
            created_at: Zoned::now(),
        }
    }

    pub async fn add_new_session(&mut self, mut session: MessageSession) -> Result<()> {
        self.sessions.remove(&session.name);

        self.notify_join(&session).await?;
        self.send_message_history(&mut session).await?;

        self.sessions.insert(session.name.clone(), session);

        Ok(())
    }

    pub async fn handle_leave(&mut self, sender_name: String) -> Result<()> {
        self.sessions.remove(&sender_name);

        let leave_packet =
            ServerPacket::create_user_left(self.name.to_string(), sender_name, Zoned::now());

        self.broadcast_packet(leave_packet).await?;
        Ok(())
    }

    pub async fn handle_client_packet(
        &mut self,
        sender_name: String,
        client_packet: ClientPacket,
    ) -> Result<()> {
        match client_packet {
            ClientPacket::Message { message, .. } => {
                self.handle_message(sender_name, message).await?
            }
            ClientPacket::Disconnect {
                room_name: _,
                sender_name,
            } => {
                self.sessions.remove(&sender_name);
                ()
            }
        }

        Ok(())
    }

    async fn handle_message(&mut self, sender_name: String, message: String) -> Result<()> {
        if message.is_empty() {
            return Err(BlankMessageError.into());
        }

        let chat_message =
            ChatMessage::new(sender_name, message, self.name.to_string(), Zoned::now());

        self.messages.push(chat_message.clone());
        self.broadcast_packet(chat_message.into()).await?;

        Ok(())
    }

    async fn broadcast_packet(&self, packet: ServerPacket) -> Result<()> {
        let _ = join_all(self.sessions.values().map(|session| {
            let packet = packet.clone();
            async move {
                session.send_packet(packet).await.unwrap_or_else(|err| {
                    tracing::trace!(
                        "Error sending packet for session {}: {:?}",
                        session.name,
                        err
                    );
                });
            }
        }))
        .await;

        Ok(())
    }

    async fn send_packet_or_disconnect(
        &mut self,
        message_session: &mut MessageSession,
        server_packet: ServerPacket,
    ) -> Result<()> {
        match message_session.send_packet(server_packet).await {
            Ok(_) => Ok(()),
            Err(_) => {
                self.handle_leave(message_session.name.clone()).await?;
                Ok(())
            }
        }
    }

    async fn send_message_history(&mut self, message_session: &mut MessageSession) -> Result<()> {
        let mut messages = self.messages.clone();
        messages.sort_by(|a, b| a.sent_at.cmp(&b.sent_at));

        for message in messages {
            self.send_packet_or_disconnect(message_session, message.into())
                .await?;
        }
        Ok(())
    }

    async fn notify_join(&mut self, message_session: &MessageSession) -> Result<()> {
        let join_packet = ServerPacket::create_user_joined(
            self.name.to_string(),
            message_session.name.to_string(),
            Zoned::now(),
        );

        self.broadcast_packet(join_packet).await?;
        Ok(())
    }

    pub fn session_count(&self) -> usize {
        self.sessions.len()
    }

    pub fn user_names(&self) -> Vec<String> {
        self.sessions.keys().cloned().collect()
    }
}
