#!/bin/bash
ps -ef|grep ${project.main.class}|grep -v grep
if [ $? -ne 0 ]
then
echo "start weather process....."
service weather start
else
echo "runing....."
fi