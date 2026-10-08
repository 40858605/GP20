# Use an official lightweight Java runtime image
FROM eclipse-temurin:17-jre

# Set the working directory inside the container
WORKDIR /tmp

# Copy the compiled executable JAR from target folder into container
COPY target/assessment_1-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Run the Java application when container starts
ENTRYPOINT ["java", "-jar", "app.jar"]