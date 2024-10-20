use crate::domain::packets::ClientPacket::Disconnect;
use serde::{Deserialize, Serialize};

#[derive(Clone, Debug)]
pub enum ClientPacket {
    Message {
        room_name: String,
        sender_name: String,
        message: String,
    },
    Disconnect {
        room_name: String,
        sender_name: String,
    },
}

#[derive(Serialize, Deserialize, Debug)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum ClientPacketType {
    Message,
    Disconnect,
}

impl ClientPacket {
    pub fn message(room_name: String, sender_name: String, message: String) -> ClientPacket {
        ClientPacket::Message {
            room_name,
            sender_name,
            message,
        }
    }

    pub fn disconnect(room_name: String, sender_name: String) -> ClientPacket {
        Disconnect {
            room_name,
            sender_name,
        }
    }
}
