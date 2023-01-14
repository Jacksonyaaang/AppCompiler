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

test_context_valide (){
    if test_synt "$1" 2>&1 | grep -q -e "$i:[0-9]*"
    then
        echo -e "${RED}[Syntax] TEST NOT PASS!!  Issue file :${ENDCOLOR}${RED_BOLD} $(basename "$1") ${SCR}"
    else
        echo -e "${GREEN}[Syntax]TEST PASS! ${ENDCOLOR} ${GREEN_BOLD} $(basename "$i") ${THUMBS_UP}"    
    fi
}

test_context_valide_decompile (){
    if decac -p "$1" 2>&1 | grep -q -e "$i:[0-9]*"
    then
        echo -e "${RED}[Decompile]TEST NOT PASS!!  Issue file :${ENDCOLOR}${RED_BOLD} $(basename "$1") ${SCR}"
    else
        echo -e "${GREEN}[Decompile] TEST PASS! ${ENDCOLOR} ${THUMBS_UP}"    
    fi
}

redirect_result_decompile(){
    decac -p "$1" > src/test/deca/syntax/valid/result/$(basename "${1%.deca}")_decompile.lis 2>&1 &
}

redirect_result(){
    test_synt "$1" > src/test/deca/syntax/valid/result/$(basename "${1%.deca}").lis 2>&1 &
}


for i in src/test/deca/syntax/valid/*.deca
do
    test_context_valide "$i"
    redirect_result "$i"
    test_context_valide_decompile "$i"
    redirect_result_decompile "$i"
done
