#!/bin/bash

if [[ $EUID -ne 1001 ]];
then
   echo "Ce script doit être lancé avec l'utilisateur VaeSoli !" 1>&2
   exit 1
else
   ./GameFreya_loop.sh &
fi