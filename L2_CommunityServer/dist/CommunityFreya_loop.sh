#!/bin/sh

while :; do
    # -server plus optimisé mais néccessite le JDK
	java -server -Dfile.encoding=UTF-8 -Xms128m -Xmx128m -cp ./../libs/*:l2jcommunity.jar com.l2jserver.communityserver.L2CommunityServer > log/stdout.log 2>&1
	sleep 10
done
