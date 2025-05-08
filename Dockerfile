FROM openjdk:23
WORKDIR /app
COPY mytradein.jar mytradein.jar
CMD ["java", "-jar", "mytradein.jar"]