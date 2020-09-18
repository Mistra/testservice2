FROM maven:3.5-jdk-8 as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn package

FROM openjdk:8u171-jre-alpine

# set deployment directory
WORKDIR /enduscan

EXPOSE 8762

ENV EUREKA_SERVER http://registry:8761/eureka
ENV MYSQL_HOST mistra.dynv6.net:3308
ENV MYSQL_USER root
ENV MYSQL_PASS enduscan

# copy over the built artifact from the maven image
COPY --from=maven target/enduscan-*.jar ./app.jar

# set the startup command to run your binary
CMD ["java", "-jar", "./app.jar"]