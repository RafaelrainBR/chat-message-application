FROM rust:1 as build-env
WORKDIR /app
COPY . /app
RUN cargo build --release

FROM gcr.io/distroless/cc-debian12

COPY --from=build-env /app/target/release/chat-message-application-backend-rust /

EXPOSE 3000

ENV TZ="America/Sao_Paulo"

CMD ["./chat-message-application-backend-rust"]
