FROM eclipse-temurin:17-jre

WORKDIR /tmp

COPY target/assessment_1-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

ENTRYPOINT ["sh", "-c", "echo 0 | java -jar app.jar"]