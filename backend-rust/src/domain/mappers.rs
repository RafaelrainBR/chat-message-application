use crate::domain::packets::{ClientPacket, ServerPacket};
use crate::domain::rooms::ChatMessage;

impl From<ChatMessage> for ClientPacket {
    fn from(value: ChatMessage) -> Self {
        ClientPacket::Message {
            room_name: value.room_name().to_string(),
            sender_name: value.sender().to_string(),
            message: value.message().to_string(),
        }
    }
}

impl From<ChatMessage> for ServerPacket {
    fn from(value: ChatMessage) -> Self {
        ServerPacket::create_message(
            value.room_name().to_string(),
            value.sender().to_string(),
            value.message().to_string(),
            value.sent_at().clone(),
        )
    }
}
