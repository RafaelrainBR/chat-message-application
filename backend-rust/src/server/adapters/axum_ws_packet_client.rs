use crate::domain::packets::Packet;
use crate::domain::rooms::PacketClient;
use async_trait::async_trait;
use tokio::sync::mpsc::Sender;

pub struct AxumWSPacketClient {
    tx: Sender<Packet>,
}

#[async_trait]
impl PacketClient for AxumWSPacketClient {
    async fn send_serialized(&self, packet: Packet) -> anyhow::Result<()> {
        self.tx.send(packet).await?;

        Ok(())
    }
}

impl AxumWSPacketClient {
    pub fn new(tx: Sender<Packet>) -> AxumWSPacketClient {
        AxumWSPacketClient { tx }
    }
}
