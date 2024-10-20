use crate::domain::packets::{ClientPacket, Packet};
use crate::domain::rooms::MessageSession;
use crate::server::adapters::AxumWSPacketClient;
use crate::server::dto::{ClientPacketDTO, ServerPacketDTO};
use crate::server::state::AppState;
use axum::body::Body;
use axum::debug_handler;
use axum::extract::ws::{Message, WebSocket};
use axum::extract::{Query, State, WebSocketUpgrade};
use axum::http::Response;
use serde::Deserialize;
use std::sync::Arc;
use tokio::sync::{mpsc, Mutex};

#[derive(Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct MessagesQuery {
    name: String,
    room: String,
}

#[debug_handler]
pub async fn messages_room_websocket_handler(
    State(state): State<Arc<AppState>>,
    Query(query): Query<MessagesQuery>,
    ws: WebSocketUpgrade,
) -> Response<Body> {
    ws.on_upgrade(|socket| handle_socket(state, socket, query))
}

async fn handle_socket(state: Arc<AppState>, mut ws: WebSocket, query: MessagesQuery) {
    let MessagesQuery { name, room } = query;
    tracing::trace!(
        "Received new connection in room [{}] with user [{}]",
        room,
        name
    );

    let (tx, mut rx) = mpsc::channel(32);
    let packet_client = Box::new(AxumWSPacketClient::new(tx.clone()));

    let session = MessageSession::new(name.clone(), room.clone(), Mutex::new(packet_client));
    state.room_storage.add_new_session(session).await.unwrap();

    let local_room_name = room.clone();
    let local_sender_name = name.clone();

    tokio::spawn(async move {
        loop {
            tokio::select! {
                ws_msg = ws.recv() => {
                    match ws_msg {
                        Some(Ok(Message::Text(text))) => {
                            let packet_dto: ClientPacketDTO = serde_json::from_str(&text).unwrap();
                            if let Ok(packet) =
                                packet_dto.into_packet(local_room_name.clone(), local_sender_name.clone())
                            {
                                tx.send(Packet::ClientPacket(packet)).await.unwrap();
                            }
                        }
                        Some(Ok(Message::Close(_close_frame))) => {
                            tracing::trace!("WS: Received close_frame for room [{}] with user [{}]", room, name);
                            let packet =
                                ClientPacket::disconnect(local_room_name.clone(), local_sender_name.clone());
                            tx.send(Packet::ClientPacket(packet)).await.unwrap();
                        }
                        Some(Ok(message)) => {
                            tracing::trace!("WS: Received other message from room [{}] with user [{}]: {:?}", room, name, message);
                        }
                        Some(Err(e)) => {
                            tracing::trace!("WS: Error receiving ws message from room [{}] with user [{}]: {:?}", room, name, e);
                            break;
                        }
                        None => {
                            tracing::trace!("WS: Socket closed on room [{}] for user [{}]", room, name);
                            let packet =
                                ClientPacket::disconnect(local_room_name.clone(), local_sender_name.clone());
                            tx.send(Packet::ClientPacket(packet)).await.unwrap();
                            break;
                        }
                    }
                },

                packet = rx.recv() => {
                    if let Some(packet) = packet {
                        match packet {
                            Packet::ClientPacket(packet) => {
                                if let Err(error) = state.room_storage.handle_client_packet(local_room_name.clone(),local_sender_name.clone(),packet,).await {
                                    tracing::trace!("RX: Got an error from handle_client_packet on room [{}] for user [{}]: {:?}", room, name, error);
                                };
                            },
                            Packet::ServerPacket(packet) => {
                                let packet_dto: ServerPacketDTO = packet.into();
                                if let Err(e) = ws.send(Message::Text(serde_json::to_string(&packet_dto).unwrap())).await {
                                    tracing::trace!("RX: Error sending message into WS on room [{}] for user [{}]: {:?}", room, name, e);
                                    break;
                                }
                            }
                        }
                    } else {
                        tracing::trace!("RX: Socket closed on room [{}] for user [{}]", room, name);
                        break;

                    }
                }
            }
        }
    });

    ()
}
