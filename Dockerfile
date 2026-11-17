FROM eclipse-temurin:21-jdk
COPY build/libs/api-docs-*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
