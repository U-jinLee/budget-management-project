#!/bin/bash

REPOSITORY=/home/ec2-user/app/
PROJECT_NAME=budget-management

cd $REPOSITORY/$PROJECT_NAME/

echo ">Git pull"
git pull

echo "> Project build start"
./gradlew build

echo "> Directory change"
cd $REPOSITORY

echo "> Build file copy"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> Now deployed application pid check"
CURRENT_PID=$(pgrep -f $PROJECT_NAME.*.jar)
echo "> Now deployed application pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> Now deployed application is not exists"
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> New application deploy"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> JAR name: $JAR_NAME"
nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 -Dspring.config.location=classpath:/application.yml &