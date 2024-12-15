# Use a base image with OpenJDK 19
FROM openjdk:19-ea-19-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the project files into the container
COPY target/indexer-1.0-SNAPSHOT.jar app.jar

# Specify the entry point for the application
CMD ["java", "-jar", "app.jar"]