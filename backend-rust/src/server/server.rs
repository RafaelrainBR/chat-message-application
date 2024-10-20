use crate::server::rest::rooms_rest_controller::list_rooms_handler;
use crate::server::state::AppState;
use crate::server::ws::messages_ws_controller::messages_room_websocket_handler;
use crate::storage::memory_storage::rooms_memory_storage::RoomMemoryStorage;
use anyhow::Result;
use axum::routing::get;
use axum::Router;
use std::sync::Arc;
use tower_http::cors::CorsLayer;
use tower_http::trace;
use tower_http::trace::TraceLayer;
use tracing::Level;

pub async fn start_server() -> Result<()> {
    tracing_subscriber::fmt()
        .with_target(false)
        .compact()
        .init();

    let room_storage = Box::new(RoomMemoryStorage::new());
    let shared_state = Arc::new(AppState {
        room_storage: Arc::new(room_storage),
    });

    let router = Router::new()
        .route("/rooms", get(list_rooms_handler))
        .route("/messages", get(messages_room_websocket_handler))
        .with_state(shared_state)
        .layer(CorsLayer::permissive())
        .layer(
            TraceLayer::new_for_http()
                .make_span_with(trace::DefaultMakeSpan::new().level(Level::INFO))
                .on_response(trace::DefaultOnResponse::new().level(Level::INFO)),
        );

    let addr = "0.0.0.0:3000";
    let listener = tokio::net::TcpListener::bind(addr).await?;

    axum::serve(listener, router.into_make_service()).await?;

    Ok(())
}
