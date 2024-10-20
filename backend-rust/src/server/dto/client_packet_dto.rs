use crate::domain::packets::{ClientPacket, ClientPacketType};
use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug)]
pub struct ClientPacketDTO {
    #[serde(rename = "type")]
    pub packet_type: ClientPacketType,
    pub message: Option<String>,
}

pub enum ClientPacketDTOError {
    MessagePacketWithoutMessage,
}

impl ClientPacketDTO {
    pub fn into_packet(
        self,
        room_name: String,
        sender_name: String,
    ) -> Result<ClientPacket, ClientPacketDTOError> {
        match self.packet_type {
            ClientPacketType::Message => {
                let message = self
                    .message
                    .ok_or(ClientPacketDTOError::MessagePacketWithoutMessage)?;

                let packet = ClientPacket::message(room_name, sender_name, message);
                Ok(packet)
            }
            ClientPacketType::Disconnect => Ok(ClientPacket::disconnect(room_name, sender_name)),
        }
    }
}
