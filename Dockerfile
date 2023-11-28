FROM openjdk:17-jdk-alpine

RUN apk add --no-cache

WORKDIR /listing-service

CMD ./gradlew run