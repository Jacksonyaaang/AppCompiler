#!/bin/bash
#one shell script to auto-test all the valide unity test for the part B
cd "$(dirname "$0")"/../.. || exit 1

PATH=./src/test/script/launchers/:"$PATH"
#PATH=./src/test/deca/context/valid/provided:"$PATH"

RED="\e[31m"
RED_BOLD="\e[1;31m"
GREEN="\e[32m"
ENDCOLOR="\e[0m"

THUMBS_UP='\U1F44D' 
SMILE='\U1F60E' 
SCR='\U1F631'

GREENFOND="\e[42m"
echo -e "${GREENFOND}AUTO VALIDE CONTEXT TEST START : ${ENDCOLOR} \n" 

test_context_valide (){
    if test_context "$1" 2>&1 | grep -q -e "$1:[0-9]*"
    then
        echo -e "${RED}TEST NOT PASS!!  Issue file :${ENDCOLOR} ${RED_BOLD} $(basename "$i") ${SCR}"
        #exit 1
    else
        echo -e "${GREEN}TEST PASS! ${ENDCOLOR} ${THUMBS_UP}"    
    fi
}

redirect_result(){
    test_context "$1" > src/test/deca/context/valid/result/$(basename "${1%.deca}").lis 2>&1 &
}

for i in src/test/deca/context/valid/*.deca
do
    test_context_valide "$i"
    redirect_result "$i"
done
