# Especifica la versión de la sintaxis del Dockerfile para habilitar las características más recientes de BuildKit
# syntax=docker/dockerfile:1

# ==========================================
# ETAPA 1: Construcción (Build Stage)
# ==========================================
# Utiliza una imagen que ya incluye Maven y el JDK de Java 21 (basada en Alpine Linux para ser más ligera)
# Se le asigna el alias "build" para referenciarla más adelante
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build

# Define el directorio de trabajo donde se compilará el código
WORKDIR /build

# Copia primero SOLO el archivo pom.xml (la definición del proyecto y dependencias en Maven)
COPY pom.xml .

# Descarga todas las dependencias de Maven necesarias para que queden cacheadas en esta capa.
# -q: Modo silencioso (menos logs). -DskipTests: Evita correr los tests en este paso.
# Hacer esto antes de copiar el código fuente ahorra mucho tiempo en futuros builds si el pom.xml no cambia.
RUN mvn -q -DskipTests dependency:go-offline

# Ahora copia la carpeta con el código fuente del proyecto
COPY src ./src

# Compila y empaqueta la aplicación en un archivo .jar (omitiendo la ejecución de los tests nuevamente)
RUN mvn -q -DskipTests package


# ==========================================
# ETAPA 2: Producción (Runtime Stage)
# ==========================================
# Inicia una nueva etapa desde cero, esta vez usando SOLO el JRE (Java Runtime Environment), no el JDK completo.
# Esto reduce drásticamente el tamaño final de la imagen y mejora la seguridad al no incluir herramientas de compilación.
FROM eclipse-temurin:21-jre-alpine

# Buenas prácticas de seguridad: Crea un grupo y un usuario del sistema sin privilegios de administrador (root) llamado "app"
# -S significa "System account" (cuenta de sistema)
RUN addgroup -S app && adduser -S -G app app

# Establece el directorio de trabajo para la aplicación terminada
WORKDIR /app

# Copia el archivo .jar generado en la etapa de construcción (alias "build") al contenedor final.
# Al mismo tiempo, le asigna la propiedad del archivo al usuario y grupo "app" (--chown=app:app) y lo renombra a "app.jar"
COPY --from=build --chown=app:app /build/target/sales-service-*.jar app.jar

# A partir de este punto, todas las instrucciones se ejecutarán usando el usuario "app" (no-root)
USER app

# Documenta que la aplicación escuchará en el puerto 8082
EXPOSE 8082

# Configura opciones fundamentales para que la JVM (Java) se comporte bien dentro de un contenedor Docker:
# - MaxRAMPercentage=75: Usa como máximo el 75% de la memoria RAM asignada al contenedor (evita que el contenedor sea matado por el sistema).
# - ExitOnOutOfMemoryError: Fuerza a que la aplicación se cierre si se queda sin memoria, permitiendo que Docker la reinicie automáticamente.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

# Comando principal para ejecutar la aplicación cuando arranque el contenedor
ENTRYPOINT ["java","-jar","app.jar"]