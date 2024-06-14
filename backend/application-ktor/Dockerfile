FROM ubuntu:latest

# Timezone setup
ENV TZ="America/Sao_Paulo"
RUN apt-get update && \
    apt-get install -yq tzdata && \
    ln -fs /usr/share/zoneinfo/$TZ /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata

WORKDIR /app
COPY build/bin/linuxX64/releaseExecutable/application-ktor.kexe /app

EXPOSE 8080

ENTRYPOINT ["./application-ktor.kexe"]
