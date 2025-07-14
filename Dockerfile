# BUILD
FROM maven:3.9.4-amazoncorretto-21 AS build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

## RUNNING

FROM amazoncorretto:21.0.5

WORKDIR /app

COPY --from=build ./build/target/*.jar /app/onlinelibraryapi.jar

EXPOSE 8080
EXPOSE 9090

ENTRYPOINT [ "java", "-jar", "onlinelibraryapi.jar" ]



