FROM openjdk:11-slim
COPY build/libs/api-docs-*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
