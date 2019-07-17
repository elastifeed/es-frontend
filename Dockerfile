FROM maven:3.6.1-jdk-13 as builder

WORKDIR /build

# Maven (cached so we dont have to do this every time)
COPY pom.xml pom.xml
RUN mvn dependency:resolve

# Copy the application source code and package it
COPY . .
RUN mvn package


FROM openjdk:13-ea-27-jdk-alpine

WORKDIR /app
COPY --from=builder /build/target/es_frontend-*-SNAPSHOT.jar es_frontend.jar
COPY ./src/main/resources/k8s/config.properties .

ENTRYPOINT ["java", "-jar", "es_frontend.jar"]

EXPOSE 8080
