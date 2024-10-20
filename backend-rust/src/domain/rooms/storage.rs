use crate::domain::packets::ClientPacket;
use crate::domain::rooms::room::Room;
use crate::domain::rooms::session::MessageSession;
use anyhow::Result;
use async_trait::async_trait;

#[async_trait]
pub trait RoomStorage {
    async fn list_rooms(&self) -> Result<Vec<RoomData>>;
    async fn get_room(&self, room_name: String) -> Result<Option<RoomData>>;
    async fn add_new_session(&self, message_session: MessageSession) -> Result<()>;
    async fn handle_leave(&self, room_name: String, sender_name: String) -> Result<()>;
    async fn handle_client_packet(
        &self,
        room_name: String,
        sender_name: String,
        client_packet: ClientPacket,
    ) -> Result<()>;
}

pub struct RoomData {
    pub name: String,
    pub session_count: usize,
    pub user_names: Vec<String>,
    pub created_at: jiff::Zoned,
}

impl From<&Room> for RoomData {
    fn from(value: &Room) -> Self {
        RoomData {
            name: value.name().to_string(),
            session_count: value.session_count(),
            user_names: value.user_names(),
            created_at: value.created_at().clone(),
        }
    }
}
