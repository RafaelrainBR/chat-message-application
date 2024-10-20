use jiff::Zoned;
use serde::{Deserialize, Serialize};

#[derive(Clone, Deserialize, Debug)]
pub struct ServerPacket {
    pub packet_type: ServerPacketType,
    pub room_name: String,
    pub sender_name: String,
    pub message: Option<String>,
    pub sent_at: Zoned,
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
}
