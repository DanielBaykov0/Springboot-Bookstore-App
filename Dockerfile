FROM maven:3.8.4-openjdk-17-slim AS build
LABEL maintainer="danibaikov@gmail.com"
WORKDIR /app
COPY . /app


#RUN apt-get update && apt-get install -y maven
#
#
#RUN mvn clean package

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/springboot-bookstore-app-0.0.1-SNAPSHOT.jar /app/springboot-bookstore-app-docker.jar
ENTRYPOINT ["java", "-jar", "springboot-bookstore-app-docker.jar"]