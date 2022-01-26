FROM adoptopenjdk/openjdk11 AS builder
WORKDIR /volume
COPY . .
RUN ./mvnw clean install

FROM adoptopenjdk/openjdk11
COPY --from=builder /volume/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
