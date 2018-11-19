
FROM maven:3.6.0-jdk-8-alpine as builder
RUN mkdir -p /code/
WORKDIR /code
COPY ./src ./src
COPY ./pom.xml ./
RUN mvn clean package

FROM openjdk:8-jdk-alpine
RUN apk --update add ca-certificates
COPY --from=builder /code/target/html-diff-service-*.jar /html-diff-service.jar
CMD ["java", "-jar", "/html-diff-service.jar"]

EXPOSE 8080
