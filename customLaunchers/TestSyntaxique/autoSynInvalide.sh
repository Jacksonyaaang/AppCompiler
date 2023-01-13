#!/bin/bash
#one shell script to auto-test all the valide unity test for the part B
cd "$(dirname "$0")"/../.. || exit 1

PATH=./src/test/script/launchers/:"$PATH"
#PATH=./src/test/deca/context/valid/provided:"$PATH"

RED="\e[31m"
GREEN="\e[32m"
ENDCOLOR="\e[0m"
RED_BOLD="\e[1;31m"

THUMBS_UP='\U1F44D' 
SMILE='\U1F60E' 
SCR='\U1F631'
GREENFOND="\e[42m"
echo -e "${GREENFOND} AUTO INVALIDE CONTEXT TEST START : ${ENDCOLOR} \n" 

test_context_invalide (){
    if test_synt "$1" 2>&1 | grep -q -e "$i:[0-9]*"
    then
        echo -e "${GREEN}TEST PASS! ${ENDCOLOR} ${THUMBS_UP}"    
    else
        echo -e "${RED}TEST NOT PASS!!  Issue file : ${ENDCOLOR}${RED_BOLD} $(basename "$1") ${SCR}"
        exit 1
    fi
}
redirect_result(){
    test_synt "$1" > src/test/deca/syntax/invalid/result/$(basename "${1%.deca}").lis 2>&1 &
}

for i in src/test/deca/syntax/invalid/*.deca
do
    test_context_invalide "$i"
    redirect_result "$i"
done
