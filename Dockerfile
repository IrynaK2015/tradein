FROM openjdk:23
WORKDIR /app
COPY out/artifacts/mytradein_jar/mytradein.jar mytradein.jar
CMD ["java", "-jar", "mytradein.jar"]