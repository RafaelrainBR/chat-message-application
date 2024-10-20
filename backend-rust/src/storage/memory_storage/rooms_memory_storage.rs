use crate::domain::packets::ClientPacket;
use crate::domain::rooms::{MessageSession, Room, RoomData, RoomStorage};
use anyhow::{Ok, Result};
use async_trait::async_trait;
use std::collections::HashMap;
use tokio::sync::Mutex;

pub struct RoomMemoryStorage {
    rooms: Mutex<HashMap<String, Room>>,
}

impl RoomMemoryStorage {
    pub fn new() -> RoomMemoryStorage {
        RoomMemoryStorage {
            rooms: Mutex::new(HashMap::new()),
        }
    }
}

#[async_trait]
impl RoomStorage for RoomMemoryStorage {
    async fn list_rooms(&self) -> Result<Vec<RoomData>> {
        Ok(self
            .rooms
            .lock()
            .await
            .values()
            .map(|room| room.into())
            .collect())
    }

    async fn get_room(&self, room_name: String) -> Result<Option<RoomData>> {
        Ok(match self.rooms.lock().await.get(&room_name) {
            None => None,
            Some(room) => Some(room.into()),
        })
    }

    async fn add_new_session(&self, message_session: MessageSession) -> Result<()> {
        let mut rooms = self.rooms.lock().await;

        let room_name = message_session.room_name.to_string();
        let room = rooms
            .entry(room_name.clone())
            .or_insert(Room::new(room_name));

        room.add_new_session(message_session).await?;

        Ok(())
    }

    async fn handle_leave(&self, room_name: String, sender_name: String) -> Result<()> {
        let mut rooms = self.rooms.lock().await;

        let room = rooms
            .entry(room_name.clone())
            .or_insert(Room::new(room_name));

        room.handle_leave(sender_name).await?;

        Ok(())
    }

    async fn handle_client_packet(
        &self,
        room_name: String,
        sender_name: String,
        client_packet: ClientPacket,
    ) -> Result<()> {
        let mut rooms = self.rooms.lock().await;

        let room = rooms
            .entry(room_name.clone())
            .or_insert(Room::new(room_name));

        room.handle_client_packet(sender_name, client_packet)
            .await?;

        Ok(())
    }
}
