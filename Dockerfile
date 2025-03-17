FROM eclipse-temurin:17-jdk-ubi9-minimal
WORKDIR /app
COPY .mvn/ .mvn
COPY src ./src
COPY mvnw pom.xml ./
RUN ./mvnw clean install -DskipTests
CMD ["./mvnw", "spring-boot:run"]