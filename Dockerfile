# Dockerfile (automation tests)
FROM maven:3.9.9-eclipse-temurin-17

ENV SUITE_NAME=api
ENV BASE_URI=http://backend:8080/

WORKDIR /app
COPY pom.xml .
COPY src src/
COPY upload_files upload_files/

RUN mvn -B -q -Dmaven.test.skip=true clean package

ENTRYPOINT ["sh","-c","mvn -B -q -DbaseUri=${BASE_URI} -DsuiteXmlFile=${SUITE_NAME} test"]
