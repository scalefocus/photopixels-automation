FROM maven:3.6.3-openjdk-17 AS build

ENV SUITE_NAME=api

WORKDIR /app

COPY pom.xml .
COPY src src/
COPY upload_files upload_files/

RUN mvn clean install -DskipTests

ENTRYPOINT ["/bin/bash", "-c", "\
if [ \"$SUITE_NAME\" = \"web\" ]; then \
  mvn clean test \
  -DbaseUri=${BASE_URI} \
  -DsuiteXmlFile=${SUITE_NAME} \
  -DwebUrl=${WEB_URL} \
  -DseleniumHubHost=${SELENIUM_GRID_URL} \
  -DisPrepareUsers=${PREPARE_USERS:-true} \
  -Dwebdriver.remote.isRemote=${IS_REMOTE:-true}; \
else \
  mvn -q clean test \
  -DbaseUri=${BASE_URI} \
  -DsuiteXmlFile=${SUITE_NAME} \
  -DlocalMailUri=${LOCAL_MAIL_URI}; \
fi"]