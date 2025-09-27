Of course, here is the `Dockerfile` code we created for your project.

```dockerfile
# Use a slim Java 17 image as the base
FROM openjdk:17-slim

# Set an argument for the JAR file path
ARG JAR_FILE=target/*.jar

# Copy the JAR file from the target folder into the container and rename it to app.jar
COPY ${JAR_FILE} app.jar

# Set the command to run the application when the container starts
ENTRYPOINT ["java","-jar","/app.jar"]
```