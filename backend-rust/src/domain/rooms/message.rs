#[derive(Clone)]
pub struct ChatMessage {
    sender: String,
    message: String,
    room_name: String,
    sent_at: jiff::Zoned,
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

    pub fn sender(&self) -> &String {
        &self.sender
    }

    pub fn message(&self) -> &String {
        &self.message
    }

    pub fn room_name(&self) -> &String {
        &self.room_name
    }

    pub fn sent_at(&self) -> &jiff::Zoned {
        &self.sent_at
    }
}
