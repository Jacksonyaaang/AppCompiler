#!/bin/bash
#one shell script to auto-test all the valide unity test for the part B
cd "$(dirname "$0")"/../.. || exit 1

PATH=./src/test/script/launchers/:"$PATH"
#PATH=./src/test/deca/context/valid/provided:"$PATH"

RED="\e[31m"
GREEN="\e[32m"
ENDCOLOR="\e[0m"

THUMBS_UP='\U1F44D' 
SMILE='\U1F60E' 
SCR='\U1F631'
GREENFOND="\e[42m"
echo -e "${GREENFOND} AUTO INVALIDE CONTEXT TEST START : ${ENDCOLOR} \n" 

test_context_valide (){
    if test_synt "$1" 2>&1 | grep -q -e "$i:[0-9]*"
    then
        echo -e "${RED}TEST NOT PASS!!  Issue file : $i ${SCR}"
        exit 1
    else
        echo -e "${GREEN}TEST PASS! ${ENDCOLOR} ${THUMBS_UP}"    
    fi
}

for i in src/test/deca/syntax/valid/personal/*.deca
do
    test_context_valide "$i"
done
