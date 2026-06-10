FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o JAR que você acabou de gerar no seu terminal físico
COPY target/walletly-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Executa o JAR direto, sem precisar de Maven
CMD ["java", "-jar", "app.jar"]