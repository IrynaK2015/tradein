FROM openjdk:23
WORKDIR /app
COPY mytradein-0.0.1-SNAPSHOT.jar mytradein.jar
CMD ["java", "-jar", "mytradein.jar"]