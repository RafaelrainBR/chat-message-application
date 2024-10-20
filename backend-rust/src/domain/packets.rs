mod client_packet;
mod server_packet;

pub use client_packet::ClientPacket;
pub use client_packet::ClientPacketType;
pub use server_packet::ServerPacket;
pub use server_packet::ServerPacketType;

#[derive(Clone, Debug)]
pub enum Packet {
    ClientPacket(client_packet::ClientPacket),
    ServerPacket(server_packet::ServerPacket),
}
