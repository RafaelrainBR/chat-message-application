use crate::server::server::start_server;

pub mod domain;
pub mod error;
pub mod server;
pub mod storage;

#[tokio::main]
async fn main() {
    start_server().await.unwrap();
}
