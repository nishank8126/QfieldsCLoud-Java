FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY target/spring-app-1.0.0.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=production

ENTRYPOINT ["java", "-jar", "/app/app.jar"]