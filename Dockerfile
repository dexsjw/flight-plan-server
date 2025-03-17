FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN --mount=type=secret,id=apikey_key,env=APIKEY_KEY
RUN --mount=type=secret,id=apikey_value,env=APIKEY_VALUE
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