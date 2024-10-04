FROM eclipse-temurin:17-jre AS runtime

# Timezone setup
ENV TZ="America/Sao_Paulo"
RUN apt-get update && \
    apt-get install -yq tzdata && \
    ln -fs /usr/share/zoneinfo/$TZ /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata

WORKDIR /app
COPY build/libs/application-ktor-java-all.jar /app/application-ktor-java-all.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/application-ktor-java-all.jar"]
