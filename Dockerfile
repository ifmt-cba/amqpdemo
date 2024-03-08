FROM openjdk:17-jdk

COPY target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]