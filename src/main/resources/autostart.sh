#!/bin/bash
# Init file for Baidu91 Weather Spider daemon
# chkconfig: 35 99 01
# description: Weather
# processname: java

export HOME=${project.path}

start()
{
  cd $HOME
  ps -ef|grep ${project.main.class}|grep -v grep
  if [ $? -ne 0 ]
  then
    echo "start weather spider....."
    echo "[START] "  `date` >> start.log
    touch /var/lock/subsys/weather
    su weather -c "bin/start.sh"
    echo "*/3 * * * * ${project.path}/bin/monitor.sh" > bin/mycron
    crontab -u weather bin/mycron
    rm -rf bin/mycron
  else
    echo "already runing....."
  fi
}

stop()
{
  cd $HOME
  echo "[STOP]  "  `date` >> start.log
  rm -f /var/lock/subsys/weather
  su weather -c "bin/stop.sh"
  crontab -r -u weather
}

case "$1" in
start)  start;;
stop)   stop;;
esac

