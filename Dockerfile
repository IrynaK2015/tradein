FROM openjdk:23
WORKDIR /app
COPY mytradein.jar mytradein.jar
CMD ["java", "-jar", "target/mytradein-0.0.1-SNAPSHOT.jar"]