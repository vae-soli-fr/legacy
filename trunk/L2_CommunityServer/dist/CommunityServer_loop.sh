#!/bin/sh

while :; do

	java -Dfile.encoding=UTF-8 -Xms128m -Xmx128m -cp ./../libs/*:l2jcommunity.jar com.l2jserver.communityserver.L2CommunityServer > log/stdout.log 2>&1
	sleep 10
done
