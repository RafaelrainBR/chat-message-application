use crate::domain::packets::{Packet, ServerPacket};
use crate::domain::rooms::packet_client::PacketClient;
use crate::error::Error;
use anyhow::Result;
use tokio::sync::Mutex;

pub struct MessageSession {
    pub name: String,
    pub room_name: String,
    packet_client: Mutex<Box<dyn PacketClient + Send + Sync>>,
}

impl MessageSession {
    pub fn new(
        name: String,
        room_name: String,
        packet_client: Mutex<Box<dyn PacketClient + Send + Sync>>,
    ) -> MessageSession {
        MessageSession {
            name,
            room_name,
            packet_client,
        }
    }

    pub async fn send_packet(&self, packet: ServerPacket) -> Result<()> {
        let packet_client = self
            .packet_client
            .try_lock()
            .map_err(|_| Error::UnknownError)?;

        packet_client
            .send_serialized(Packet::ServerPacket(packet))
            .await
    }
}
