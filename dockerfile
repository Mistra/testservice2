FROM maven:3.5-jdk-8 as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn package

FROM openjdk:8u171-jre-alpine

# copy over the built artifact from the maven image
COPY --from=maven target/service2-*.jar ./app.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./app.jar"]