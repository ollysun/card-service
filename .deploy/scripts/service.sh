#!/bin/bash
if [ -f env.sh ]; then
    . env.sh
fi
LOG_FILE=$LOG_DIR/cardIdentification.log AWS_SECRETSMANAGER_REGION=$AWS_REGION SERVICE_NAME=$SERVICE_NAME /usr/bin/java -Xmx1g -Dfile.encoding=UTF-8 \
-Djava.net.preferIPv4Stack=true -Duser.timezone=UTC $JAVA_OPTS -Dspring.profiles.include=sslEnabled,aws -jar cardIdentification.jar
