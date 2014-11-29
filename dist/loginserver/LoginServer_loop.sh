#!/bin/sh

while :;
do
	java -server -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 -Xmx64m -cp config/xml:../gameserver/libs/*: lineage2.loginserver.LoginServer > log/stdout.log 2>&1

	[ $? -ne 2 ] && break
	sleep 10;
done
