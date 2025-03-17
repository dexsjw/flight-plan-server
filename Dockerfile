FROM maven:3.9.9-eclipse-temurin-17
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/flight-plan-server-0.0.1-SNAPSHOT.jar"]

#FROM eclipse-temurin:17-jdk-ubi9-minimal
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY src ./src
#COPY mvnw pom.xml ./
#RUN ./mvnw clean install -DskipTests
#CMD ["./mvnw", "spring-boot:run"]