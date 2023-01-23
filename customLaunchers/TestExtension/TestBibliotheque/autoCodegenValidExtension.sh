#!/bin/bash
#Shell script to test code generaton and correct output
cd "$(dirname "$0")"/../../..|| exit 1

PATH=./src/main/bin/:"$PATH"

RED="\e[31m"
GREEN="\e[32m"
ENDCOLOR="\e[0m"
RED_BOLD="\e[1;31m"

THUMBS_UP='\U1F44D' 
SMILE='\U1F60E' 
SCR='\U1F631'
GREENFOND="\e[42m"
echo -e "${GREENFOND} AUTO valide codegen TEST START : ${ENDCOLOR} \n" 

test_codegen_valide (){
    # echo decac "$1"
    if decac -r "$2" "$1" 2>&1 | grep -q -e "$i:[0-9]*"
    then
        echo -e "${RED} TEST DID NOT PASS! FILE DID NOT COMPILE :${ENDCOLOR}${RED_BOLD} $(basename "$1") ${SCR}"
        #exit 1
    else
        echo -e "${GREEN}[Codegen] TEST PASS! FILE COMPILED  ${ENDCOLOR} ${THUMBS_UP}"    
    fi
}

redirect_result(){
    if grep "/test_Readoperation.ass"  <<< $1
    then  
        ima "$1" < src/test/deca/library/valid/Input_read_operation.txt > src/test/deca/library/valid/result/$(basename "${1%.ass}")_output.res 
    else 
        ima "$1" > src/test/deca/library/valid/result/$(basename "${1%.ass}")_output.res 
        compare_output src/test/deca/library/valid/result/$(basename "${1%.ass}")_output.res
    fi
}

compare_output(){
    # echo "-----------FILE ONE--------------"
    # echo "$1"
    # cat "$1"
    # echo "---------FILE TWO----------------"
    # echo ${1%.res}_expected.res
    # cat ${1%.res}_expected.res
    # echo "$1" ${1%.res}_expected.res
    if test -f ${1%.res}_expected.res; then
        if cmp -s "$1" ${1%.res}_expected.res; then
            echo  -e "${GREEN}[output] Program output is correct $1 for %s${ENDCOLOR} ${THUMBS_UP}" 
        else
            echo -e "${RED}[output] Program output is NOT correct $1 for %s${ENDCOLOR} " 
        fi
    else 
        echo  -e "${GREEN}[output] Compare file does not exists for $i ${ENDCOLOR} " 
    fi
}


for i in src/test/deca/library/valid/*.deca
do
    #echo "$i"
    test_codegen_valide "$i" "16"
done


for i in src/test/deca/library/valid/*.ass
do
    # echo "$i"
    redirect_result "$i"
done


for i in src/test/deca/library/valid/*.deca
do
    echo "----------HARD----------- decac -r 4"
    test_codegen_valide "$i" "4"
done


for i in src/test/deca/library/valid/*.ass
do
    # echo "$i"
    redirect_result "$i"
done
