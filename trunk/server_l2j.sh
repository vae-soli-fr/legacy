#!/bin/bash

####################################
#                                  #
# Script utiliser pour demarer     #
# arreter, connaitre le status de  #
# L2J et voir les logs du          #
# LoginServer and GameServer.      #
#                                  #
# Fait Initialement par Respawner  #
# Remasteriser et ameliore par     #
# - Dragondark                     #
####################################

####################################
#          Modification            #
####################################

PATH_GS="/PATH/TO/gameserver"
PATH_LS="/PATH/TO/login"

####################################
#           Variables              #
####################################

LS_NAME="loginserver.L2LoginServer"
GS_NAME="gameserver.GameServer"
LS_LOOP="LoginServer_loop.sh"
GS_LOOP="GameServer_loop.sh"
ROUGE="$(tput bold ; tput setaf 1)"
RESETCOLOR="$(tput sgr0)"
VERT="$(tput bold ; tput setaf 2)"
JAUNE="$(tput bold ; tput setaf 3)"
CYAN="$(tput bold ; tput setaf 6)"

# LoginServer
LL_PID=`ps auxw | grep -v grep | grep $LS_LOOP | awk ' { print $2 } '`
LS_PID=`ps auxw | grep -v grep | grep $LS_NAME | awk ' { print $2 } '`
# GameServer
GL_PID=`ps auxw | grep -v grep | grep $GS_LOOP | awk ' { print $2 } '`
GS_PID=`ps auxw | grep -v grep | grep $GS_NAME | awk ' { print $2 } '`

####################################
#           Fonctions              #
####################################



####################################
#            Fonction              #
#            server_stop()         #
####################################
# permet l'arret du                #
# loginserver et gameserver si ils #
# sont deja lance                  #
####################################

server_stop()
{
   echo
   echo "+---------------------------------------+"
   echo "|$CYAN Stopping L2J LoginServer & GameServer$RESETCOLOR |"
   echo "+---------------------------------------+"
   echo
   if [[ $LL_PID ]]; then
        kill $LL_PID
        echo "Arret du Script du LoginServer                       ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Le script du LoginServer n'est pas en fonctionnement ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   if [[ $LS_PID ]]; then
        kill $LS_PID
        echo "Arret du LoginServer                                 ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Votre LoginServer n'est pas en fonctionnement        ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   if [[ $GL_PID ]]; then
        kill $GL_PID
        echo "Arret du Script du GameServer                        ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Le script du GameServer n'est pas en fonctionnement  ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   if [[ $GS_PID ]]; then
        kill $GS_PID
        echo "Arret du GameServer                                  ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Votre GameServer n'est pas en fonctionnement         ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   echo
   echo "+---------------------------------------+"
   echo
}

####################################
#            Fonction              #
#            gamserver_stop()      #
####################################
# permet l'arret du                #
# gameserver si il est deja lance  #
####################################

gamserver_stop()
{
   echo
   echo "+-------------------------+"
   echo "|$CYAN Stopping L2J GameServer$RESETCOLOR |"
   echo "+-------------------------+"
   echo
   if [[ $GL_PID ]]; then
        kill $GL_PID
        echo "Arret du Script du GameServer                        ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Le script du GameServer n'est pas en fonctionnement  ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   if [[ $GS_PID ]]; then
        kill $GS_PID
        echo "Arret du GameServer                                  ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Votre GameServer n'est pas en fonctionnement         ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   echo
   echo "+---------------------------------------+"
   echo

}

####################################
#            Fonction              #
#            loginserver_stop()    #
####################################
# permet l'arret du                #
# LoginServer si il est deja lance #
####################################

loginserver_stop()
{
   echo
   echo "+---------------------------+"
   echo "|$CYAN Stopping L2J LoginServer $RESETCOLOR |"
   echo "+---------------------------+"
   echo
   if [[ $LL_PID ]]; then
        kill $LL_PID
        echo "Arret du Script du LoginServer                       ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Le script du LoginServer n'est pas en fonctionnement ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   if [[ $LS_PID ]]; then
        kill $LS_PID
        echo "Arret du LoginServer                                 ["$VERT"OK"$RESETCOLOR"]"
   else
        echo "Votre LoginServer n'est pas en fonctionnement        ["$ROUGE"FAIL"$RESETCOLOR"]"
   fi
   echo
   echo "+---------------------------------------+"
   echo
}
####################################
#            Fonction              #
#            server_start()        #
####################################
# permet le lancement du           #
# loginserver et gameserver si ils #
# ne sont pas deja lance           #
####################################

server_start()
{
   echo
   echo "+---------------------------------------+"
   echo "|$CYAN Starting L2J LoginServer & GameServer$RESETCOLOR |"
   echo "+---------------------------------------+"
   if [[ $LL_PID || $LL_PID ]]; then
        echo "LoginServer allready running                        ["$ROUGE"FAIL"$RESETCOLOR"]"
   else
      cd $PATH_GS && ./startLoginServer.sh&
      echo "LoginServer                                           ["$VERT"OK"$RESETCOLOR"]"
      sleep 10
   fi
   if [[ $GL_PID || $GS_PID ]]; then
        echo "GameServer allready running                         ["$ROUGE"FAIL"$RESETCOLOR"]"
   else
      cd $PATH_GS && ./startGameServer.sh&
      echo "GameServer                                            ["$VERT"OK"$RESETCOLOR"]"
   fi
   echo
   echo "+---------------------------------------+"
   echo
}

####################################
#            Fonction              #
#            server_status()       #
####################################
# permet l'afficage du status du   #
# serveur et les Pid utilise       #
####################################

server_status()
{
   echo
   echo "+---------------------------------------+"
   echo "| $CYAN       L2J : Running process    $RESETCOLOR      |"
   echo "+---------------------------------------+"
   echo
   echo "- LoginServer Status -"
   if [ " $LL_PID" = " " ]; then
      echo "LoginServer script is$ROUGE OFF$RESETCOLOR"
   else
           echo "LoginServer script is$VERT ON$RESETCOLOR (PID is $JAUNE$LL_PID$RESETCOLOR)"
       fi
       if [ " $LS_PID" = " " ]; then
           echo "LoginServer Java is$ROUGE OFF$RESETCOLOR"
        else
           echo "LoginServer Java is$VERT ON$RESETCOLOR (PID is $JAUNE$LS_PID$RESETCOLOR)"
        fi
        echo
        echo "- GameServer Status -"
        if [ " $GL_PID" = " " ]; then
           echo "GameServer script is$ROUGE OFF$RESETCOLOR"
        else
           echo "GameServer script is$VERT ON$RESETCOLOR (PID is $JAUNE$GL_PID$RESETCOLOR)"
        fi
        if [ " $GS_PID" = " " ]; then
           echo "GameServer Java is$ROUGE OFF$RESETCOLOR"
        else
           echo "GameServer Java is$VERT ON$RESETCOLOR (PID is $JAUNE$GS_PID$RESETCOLOR)"
        fi
   echo
   echo "+---------------------------------------+"
}

####################################
#            Fonction              #
#            loginserver()         #
####################################
# permet l'afficage de des logs du #
# Loginserver                      #
####################################

loginserver()
{
   echo
   echo "+----------------------+"
   echo "|$CYAN L2J LoginServer Logs$RESETCOLOR |"
   echo "|$CYAN   Quit by Ctrl + C  $RESETCOLOR |"
   echo "+----------------------+"
   tail -f $PATH_LS/log/stdout.log
}

####################################
#            Fonction              #
#            gameserver()          #
####################################
# permet l'afficage de des logs du #
# Gamerserver                      #
####################################

gameserver()
{
   echo
   echo "+---------------------+"
   echo "|$CYAN L2J GameServer Logs$RESETCOLOR |"
   echo "|$CYAN  Quit by Ctrl + C  $RESETCOLOR |"
   echo "+---------------------+"
   tail -f $PATH_GS/log/stdout.log
}

####################################
#            Fonction              #
#            log_all()             #
####################################
# permet l'afficage de des logs du #
# Gamerserver et loginserver       #
####################################

log_all()
{
   echo
   echo "+-------------------------------------+"
   echo "|$CYAN L2J GameServer AND LoginServer Logs$RESETCOLOR |"
   echo "|$CYAN         Quit by Ctrl + C           $RESETCOLOR |"
   echo "+-------------------------------------+"
   tail -f $PATH_GS/log/stdout.log $PATH_LS/log/stdout.log
}

####################################
#           Traitements            #
####################################

if [[ $# -ne 1 && $# -ne 2 ]]; then
   echo "Usage: $0 {start|stop|log|status} [gs|ls|login|gameserver]"
   exit 0
fi

if [ $# -eq 2 ]; then
        if [ $1 == "log" ]; then
                case $2 in
                        login) loginserver;;
                        ls) loginserver;;
                        gs) gameserver;;
                        gameserver) gameserver;;
                        *) log_all;;
                esac
        elif [ $1 == "stop" ]; then
                case $2 in
                        login) loginserver_stop;;
                        ls) loginserver_stop;;
                        gs) gamserver_stop;;
                        gameserver) gamserver_stop;;
                        *) server_stop;;
                esac
        else
                echo "Usage: $0 {start|stop|log|status} [gs|ls|login|gameserver]"
        fi
else

case $1 in
   start)  server_start;;
   stop)  server_stop;;
   status) server_status;;
   *)      echo "Usage: $0 {start|stop|log|status} [gs|ls|login|gameserver]";;
esac

fi

exit 0
