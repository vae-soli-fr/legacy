#!/bin/bash

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do
        if test -f "updater/l2jserver.jar"; then
          cp "l2jserver.jar" "l2jserver.`date +%d-%m-%Y_%H-%M`.bak"
          mv "updater/l2jserver.jar" "l2jserver.jar"
            if test -f "updater/l2j-version.properties"; then
            mv -f "updater/l2j-version.properties" "config/l2j-version.properties"
            fi
        fi
        if test -f "updater/l2jdp-version.properties"; then
          mv -f "updater/l2jdp-version.properties" "config/l2jdp-version.properties"
        fi
	[ -f log/java0.log.0 ] && mv log/java0.log.0 "log/`date +%Y-%m-%d_%H-%M-%S`_java.log"
	[ -f log/stdout.log ] && mv log/stdout.log "log/`date +%Y-%m-%d_%H-%M-%S`_stdout.log"
	# -server plus optimisé
        java -server -Dfile.encoding=UTF-8 -Djava.util.logging.manager=com.l2jserver.util.L2LogManager -Xms1280m -Xmx1280m -cp ./../libs/*:l2jserver.jar com.l2jserver.gameserver.GameServer > log/stdout.log 2>&1
	[ $? -ne 2 ] && break
#	/etc/init.d/mysql restart
	sleep 10
done