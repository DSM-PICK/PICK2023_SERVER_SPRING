FROM eclipse-temurin:17-jre-focal

EXPOSE 8080

ARG CLOUD_CONFIG_USERNAME
ENV CLOUD_CONFIG_USERNAME ${CLOUD_CONFIG_USERNAME}
ARG CLOUD_CONFIG_PASSWORD
ENV CLOUD_CONFIG_PASSWORD ${CLOUD_CONFIG_PASSWORD}
ARG PROFILE
ENV PROFILE ${PROFILE}

ADD /pick-infrastructure/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]