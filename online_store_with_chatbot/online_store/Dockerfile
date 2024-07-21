FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean
RUN ./mvnw install -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]

## Builder stage
#FROM eclipse-temurin:17-jdk-alpine as builder
#WORKDIR /opt/app
#
## Copy Maven wrapper and the pom file
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
## Fetch dependencies to go offline
#RUN ./mvnw dependency:go-offline
#
## Copy the source code
#COPY ./src ./src
#
## Clean and build the application, skipping tests
#RUN ./mvnw clean install -Dmaven.test.skip=true
#
## Runtime stage
#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /opt/app
#
## Copy the built jar from the builder stage
#COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar
#
## Expose the application port
#EXPOSE 8080
#
## Run the application
#ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
