#!/bin/bash
################################
# main
################################
# FRAMEWORK_HOME必填
FRAMEWORK_HOME=$(cd "$(dirname "$0")"; cd ..; pwd)

FRAMWORK_MAIN_CLASS="${project.main.class}"
FRAMEWORK_CLASSPATH=""
opt_conf=""
args=""

# make FRAMEWORK_HOME absolute
if [ -n "${FRAMEWORK_HOME}" ]; then
  FRAMEWORK_HOME=$(cd $FRAMEWORK_HOME; pwd)
  opt_conf="${FRAMEWORK_HOME}/conf"
else
  echo "FRAMEWORK_HOME is not set!"
  exit 1
fi

cd $FRAMEWORK_HOME

# find java
if [ -z "${JAVA_HOME}" ] ; then
  echo "JAVA_HOME is not set!"
  exit 1
fi

FRAMEWORK_CLASSPATH="${opt_conf}:${FRAMEWORK_HOME}/lib/*"

run_framework() {
  echo "java -cp $FRAMEWORK_CLASSPATH $FRAMWORK_MAIN_CLASS"
  $JAVA_HOME/bin/java -cp $FRAMEWORK_CLASSPATH $FRAMWORK_MAIN_CLASS || exit 1
}

run_framework

exit 0
