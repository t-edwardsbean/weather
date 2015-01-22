#!/bin/bash
FRAMWORK_MAIN_CLASS="${project.main.class}"
if [ -z $FRAMWORK_MAIN_CLASS ]; then
  echo "FRAMWORK_MAIN_CLASS为空！"
  exit 1
fi
echo "kill $FRAMWORK_MAIN_CLASS"
ps -ef|grep $FRAMWORK_MAIN_CLASS|grep -v grep|cut -c 9-15|xargs kill -15