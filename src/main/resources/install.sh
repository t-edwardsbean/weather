#!/bin/bash
echo "add weather spider autostart script"
cp ${project.path}/bin/autostart.sh /etc/rc.d/init.d/weather
chmod a+x /etc/rc.d/init.d/weather
echo "add weather spider as system service"
/sbin/chkconfig --add /etc/rc.d/init.d/weather