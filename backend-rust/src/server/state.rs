use crate::domain::rooms::RoomStorage;
use std::sync::Arc;

#[derive(Clone)]
pub struct AppState {
    pub room_storage: Arc<Box<dyn RoomStorage + Send + Sync>>,
}
