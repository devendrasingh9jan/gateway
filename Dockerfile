FROM maven:3.9.6 as builder
COPY . /gateway/app
WORKDIR /gateway/app
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /gateway/app
COPY --from=builder /gateway/app/target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
