FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# If you use Gradle Wrapper
COPY gradlew /home/gradle/gradlew
COPY gradle /home/gradle/gradle
RUN chmod +x /home/gradle/gradlew
RUN /home/gradle/gradlew buildFatJar --no-daemon --stacktrace

FROM openjdk:17
EXPOSE 8080
RUN mkdir -p /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/link-saver_server.jar
COPY openapi /openapi
ENTRYPOINT ["java", "-jar", "/app/link-saver_server.jar"]
