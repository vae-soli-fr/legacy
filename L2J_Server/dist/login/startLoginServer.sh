#!/bin/bash

if [[ $EUID -ne 1000 ]];
then
   echo "Ce script doit être lancé avec l'utilisateur VaeSoli !" 1>&2
   exit 1
else
   ./LoginHighFive_loop.sh &
fi