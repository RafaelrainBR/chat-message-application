use crate::domain::rooms::RoomData;
use serde::Serialize;

#[derive(Serialize)]
#[serde(rename_all = "camelCase")]
pub struct RoomDTO {
    pub name: String,
    pub length: usize,
    pub users: Vec<String>,
    pub created_at: String,
}

impl From<&RoomData> for RoomDTO {
    fn from(value: &RoomData) -> Self {
        RoomDTO {
            name: value.name.to_string(),
            length: value.session_count,
            users: value.user_names.clone(),
            created_at: value.created_at.to_string(),
        }
    }
}
