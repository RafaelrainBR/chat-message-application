use crate::server::dto::RoomDTO;
use crate::server::error::ServerError;
use crate::server::state::AppState;
use axum::extract::State;
use std::sync::Arc;

pub async fn list_rooms_handler(
    State(state): State<Arc<AppState>>,
) -> Result<axum::Json<Vec<RoomDTO>>, ServerError> {
    let room_data_list = state.room_storage.list_rooms().await?;

    let room_dto_list = room_data_list.iter().map(From::from).collect();

    Ok(axum::Json(room_dto_list))
}
