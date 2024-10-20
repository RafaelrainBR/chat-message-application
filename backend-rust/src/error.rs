#[derive(Debug, thiserror::Error)]
pub enum Error {
    #[error("Unknown error")]
    UnknownError,
    #[error("Blank message")]
    BlankMessageError,
}
