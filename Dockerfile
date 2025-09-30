FROM maven:3.6.3-openjdk-17 AS build

ENV SUITE_NAME=api

WORKDIR /app

COPY pom.xml .
COPY src src/
COPY upload_files upload_files/

RUN mvn clean install -DskipTests

ENTRYPOINT ["/bin/bash", "-c", "if [ \"$SUITE_NAME\" == \"frontend\" ]; then mvn -DsuiteXmlFile=${SUITE_NAME} test -Dwebdriver.remote.url=$SELENIUM_GRID_URL -Dwebdriver.remote.isRemote=true; else mvn -q -DbaseUrl=$BASE_URI -DsuiteXmlFile=${SUITE_NAME} -DlocalMailUri=$LOCAL_MAIL_URI test; fi"]