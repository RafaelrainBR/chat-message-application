use crate::domain::packets::Packet;
use anyhow::Result;
use async_trait::async_trait;

#[async_trait]
pub trait PacketClient {
    async fn send_serialized(&self, server_packet: Packet) -> Result<()>;
}
