# Usa a imagem base
FROM eclipse-temurin:22-jdk-jammy AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Dá permissão de execução ao mvnw
RUN chmod +x mvnw

# Compila o projeto sem rodar os testes
RUN ./mvnw clean package -DskipTests

# Cria a imagem final apenas com o necessário
FROM eclipse-temurin:22-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/rebone_3d-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
