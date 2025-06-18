FROM maven:3.6.3-openjdk-17 AS build 

ENV SUITE_NAME=api

WORKDIR /app

COPY pom.xml .
COPY src src/
COPY upload_files upload_files/

RUN mvn clean install -DskipTests

ENTRYPOINT ["/bin/sh", "-c", "mvn -B -q -DbaseUri=$BASE_URI -DsuiteXmlFile=$SUITE_NAME test"]