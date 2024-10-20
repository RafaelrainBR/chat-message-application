#[derive(Clone)]
pub struct ChatMessage {
    pub sender: String,
    pub message: String,
    pub room_name: String,
    pub sent_at: jiff::Zoned,
}

impl ChatMessage {
    pub fn new(
        sender: String,
        message: String,
        room_name: String,
        sent_at: jiff::Zoned,
    ) -> ChatMessage {
        ChatMessage {
            sender,
            message,
            room_name,
            sent_at,
        }
    }
}
