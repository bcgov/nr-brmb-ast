FROM tomcat:9.0.86-jre17

COPY ./target/farms-legacy-1.0-SNAPSHOT.war .
COPY ./deploy-tools ./deploy-tools

RUN ls

ENV TOMCAT_HOME=/usr/local/tomcat \
  CATALINA_HOME=/usr/local/tomcat \
  CATALINA_OPTS='-Xmx400m -Xms200m -server' \
  VIRUS_SCAN_DIR=/app6/apps/data/farm/dev

RUN rm -rf /usr/local/tomcat/webapps/ROOT  &&\
  mkdir /usr/local/tomcat/webapps/farms-legacy &&\
  apt-get update && apt-get install -y unzip &&\
  unzip -d /usr/local/tomcat/webapps/farms-legacy/ '*.war' &&\
  cp ./deploy-tools/context.xml /usr/local/tomcat/webapps/farms-legacy/META-INF/context.xml &&\
  cp ./deploy-tools/setenv.sh /usr/local/tomcat/bin/setenv.sh &&\
  cp ./deploy-tools/server.xml /usr/local/tomcat/conf/server.xml &&\
  cp ./deploy-tools/log4j2.xml /usr/local/tomcat/webapps/farms-legacy/WEB-INF/classes/log4j2.xml &&\
  mkdir -p /app6/userInfoFileLocation &&\
  cp ./deploy-tools/ora2-webade-user-info.xml /app6/userInfoFileLocation/ora2-webade-user-info.xml &&\
  adduser --system tomcat &&\
  chown -R tomcat:0 `readlink -f ${CATALINA_HOME}` &&\
  chmod -R 770 `readlink -f ${CATALINA_HOME}` &&\
  chown -h tomcat:0 ${CATALINA_HOME} &&\
  mkdir -p ${VIRUS_SCAN_DIR} &&\
  chown -R tomcat:0 ${VIRUS_SCAN_DIR} &&\
  chmod -R 770 ${VIRUS_SCAN_DIR} &&\
  rm /etc/localtime &&\
  ln -s /usr/share/zoneinfo/America/Vancouver /etc/localtime &&\
  export SECRET_KEY=$(openssl rand -base64 32) &&\
  keytool -genkey -noprompt -validity 365 -alias tomcat -keyalg RSA -keystore conf/selfsigned.jks -keysize 2048 -dname "cn=Unknown, ou=Unknown, o=Unknown, c=Unknown" -keypass $SECRET_KEY -storepass $SECRET_KEY &&\
  sed -i "s@SECRET_KEY@$SECRET_KEY@" conf/server.xml

USER tomcat

EXPOSE 8005 8080
CMD ["catalina.sh", "run"]
