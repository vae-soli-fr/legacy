#!/bin/sh

# exit codes of GameServer:
#  0 normal shutdown
#  2 reboot attempt

while :; do
	if test -f "updater/lineage2-gameserver.jar"; then
		cp "../libs/lineage2-gameserver.jar" "../libs/lineage2-gameserver.`date +%d-%m-%Y_%H-%M`.bak"
		mv "updater/lineage2-gameserver.jar" "../libs/lineage2-gameserver.jar"
			if test -f "updater/lineage2-version.ini"; then
			mv -f "updater/lineage2-version.ini" "config/lineage2-version.ini"
			fi
	fi
	java -server -Dfile.encoding=UTF-8 -Xmx8G -cp config/xml:../libs/*: lineage2.gameserver.GameServer > log/stdout.log 2>&1
	[ $? -ne 2 ] && break
	sleep 30;
done
