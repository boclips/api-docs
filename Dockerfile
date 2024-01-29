FROM eclipse-temurin:17-jdk
COPY build/libs/api-docs-*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
