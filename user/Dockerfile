FROM gradle:7.6-alpine AS build
ENV APP_HOME=/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME
COPY src $APP_HOME/src/
RUN gradle build

FROM openjdk:17-oracle
ENV APP_HOME=/app/
ENV ARTIFACT_NAME=user-0.0.1-SNAPSHOT.jar
ARG JAR_FILE=$APP_HOME/build/libs/$ARTIFACT_NAME
COPY --from=build $JAR_FILE /opt/user/app.jar
ENTRYPOINT ["java", "-jar", "/opt/user/app.jar"]