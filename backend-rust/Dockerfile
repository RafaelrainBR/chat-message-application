FROM rust:1 AS builder

# create a new empty project
RUN USER=root cargo new --bin app
WORKDIR /app

# copy over your manifests
COPY ./Cargo.lock ./Cargo.lock
COPY ./Cargo.toml ./Cargo.toml

# this build step will cache your dependencies
RUN cargo build --release
RUN rm src/*.rs

# copy your source tree
COPY ./src ./src

# build for release
RUN cargo build --release

FROM rust:1-alpine

WORKDIR /app
COPY --from=builder /app/target/release/chat-message-application-backend-rust /app/app

EXPOSE 3000

ENV TZ="America/Sao_Paulo"

CMD ["/app/app"]
