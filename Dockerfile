FROM xsiph/clippit AS clippit

FROM xsiph/cattit AS cattit

FROM adoptopenjdk/openjdk11:alpine AS builder
WORKDIR /volume
COPY . .
COPY --from=clippit /clippit /usr/bin/
COPY --from=cattit /cattit /usr/bin/
RUN apk add --no-cache ffmpeg
ENV PATH=$PATH:/usr/bin/
RUN ./mvnw clean install

FROM adoptopenjdk/openjdk11:alpine
COPY --from=builder /volume/target/*.jar /app.jar
COPY --from=builder /usr/bin/clippit /usr/bin/
COPY --from=builder /usr/bin/cattit /usr/bin/
RUN apk add --no-cache ffmpeg
ENV PATH=$PATH:/usr/bin/
ENTRYPOINT ["java", "-jar", "/app.jar"]
