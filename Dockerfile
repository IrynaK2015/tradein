FROM openjdk:23
WORKDIR /app
COPY app.jar app.jar
CMD ["java", "-jar", "app.jar"]