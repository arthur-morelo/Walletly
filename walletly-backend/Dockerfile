FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml .

COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src/ src/

RUN ./mvnw package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/walletly-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080