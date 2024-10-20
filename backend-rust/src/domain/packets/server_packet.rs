use jiff::Zoned;
use serde::{Deserialize, Serialize};

#[derive(Clone, Deserialize, Debug)]
pub struct ServerPacket {
    packet_type: ServerPacketType,
    room_name: String,
    sender_name: String,
    message: Option<String>,
    sent_at: Zoned,
}

#[derive(Clone, Serialize, Deserialize, Debug)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum ServerPacketType {
    Message,
    UserJoined,
    UserLeft,
}

impl ServerPacket {
    pub fn create_message(
        room_name: String,
        sender_name: String,
        message: String,
        sent_at: Zoned,
    ) -> ServerPacket {
        ServerPacket {
            packet_type: ServerPacketType::Message,
            room_name,
            sender_name,
            message: Some(message),
            sent_at,
        }
    }

    pub fn create_user_joined(
        room_name: String,
        sender_name: String,
        sent_at: Zoned,
    ) -> ServerPacket {
        ServerPacket {
            packet_type: ServerPacketType::UserJoined,
            room_name,
            sender_name,
            message: None,
            sent_at,
        }
    }

    pub fn create_user_left(
        room_name: String,
        sender_name: String,
        sent_at: Zoned,
    ) -> ServerPacket {
        ServerPacket {
            packet_type: ServerPacketType::UserLeft,
            room_name,
            sender_name,
            message: None,
            sent_at,
        }
    }

    pub fn packet_type(&self) -> ServerPacketType {
        self.packet_type.clone()
    }

    pub fn room_name(&self) -> String {
        self.room_name.clone()
    }

    pub fn sender_name(&self) -> String {
        self.sender_name.clone()
    }

    pub fn message(&self) -> Option<String> {
        self.message.clone()
    }

    pub fn sent_at(&self) -> Zoned {
        self.sent_at.clone()
    }
}
