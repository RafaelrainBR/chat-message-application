use crate::domain::packets::{ServerPacket, ServerPacketType};
use jiff::civil::DateTime;
use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ServerPacketDTO {
    #[serde(rename = "type")]
    packet_type: ServerPacketType,
    room_name: String,
    sender_name: String,
    message: Option<String>,
    sent_at: DateTime,
}

impl From<ServerPacket> for ServerPacketDTO {
    fn from(value: ServerPacket) -> Self {
        Self {
            packet_type: value.packet_type,
            room_name: value.room_name,
            sender_name: value.sender_name,
            message: value.message,
            sent_at: value.sent_at.datetime(),
        }
    }
}
