FROM openjdk:8-jdk-alpine as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} eventoapp.jar
RUN java -Djarmode=layertools -jar eventoapp.jar extract

FROM openjdk:8-jdk-alpine
WORKDIR application
EXPOSE 8443
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
