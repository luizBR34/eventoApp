# cache as most as possible in this multistage dockerfile.
FROM maven:3.6-alpine as DEPS

WORKDIR /app

COPY ./boot/pom.xml ./boot/pom.xml
COPY ./clients/pom.xml ./clients/pom.xml
COPY ./configs/pom.xml ./configs/pom.xml
COPY ./mappers/pom.xml ./mappers/pom.xml
COPY ./models/pom.xml ./models/pom.xml
COPY ./services/pom.xml ./services/pom.xml
COPY ./web/pom.xml ./web/pom.xml

# if you have modules that depends each other, you may use -DexcludeArtifactIds as follows
# RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=module1

COPY pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

# Copy the dependencies from the DEPS stage with the advantage
# of using docker layer caches. If something goes wrong from this
# line on, all dependencies from DEPS were already downloaded and
# stored in docker's layers.
FROM maven:3.6-alpine as BUILDER
WORKDIR /app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps / /
COPY ./boot/src ./boot/src
COPY ./clients/src ./clients/src
COPY ./configs/src ./configs/src
COPY ./mappers/src ./mappers/src
COPY ./models/src ./models/src
COPY ./services/src ./services/src
COPY ./web/src ./web/src

# use -o (--offline) if you didn't need to exclude artifacts.
# if you have excluded artifacts, then remove -o flag
RUN mvn -U -f /app/pom.xml -B -e clean package -DskipTests=true

# At this point, BUILDER stage should have your .jar or whatever in some path
FROM openjdk:8-alpine
WORKDIR /app/target
RUN java -Djarmode=layertools -jar *.jar extract

FROM openjdk:8-alpine
WORKDIR /app
COPY --from=builder app/boot/target/ ./
ADD ./*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
