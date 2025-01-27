FROM docker.io/library/openjdk:21
COPY ./build/libs/backend-*.jar crm-backend.jar
EXPOSE 8080
ENV CONFIG_ENV=prod
ENTRYPOINT java -jar -Dspring.profiles.active=${CONFIG_ENV} crm-backend.jar