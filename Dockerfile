# ==============================================================================
# STAGE 1: Build Spring Boot Backend với Maven
# ==============================================================================
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml ./
# Tải dependency trước để tận dụng Docker cache
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ==============================================================================
# STAGE 2: Image Runtime siêu nhẹ chỉ chạy file JAR cuối cùng
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
