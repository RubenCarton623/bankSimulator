FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /app

# Copiar los archivos de Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Establecer permisos de ejecución para mvnw
RUN chmod +x ./mvnw

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests

# Crear imagen final
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Crear directorio para logs
RUN mkdir -p /var/log/rvera

# Copiar el JAR compilado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE ${SERVER_PORT}

# Punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]