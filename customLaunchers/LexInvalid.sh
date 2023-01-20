#!/bin/sh
#Le deuxiéme fichier ne retourne pas une erreur pour test_lex
for i in ../src/test/deca/syntax/invalid/provided/*.deca
do
echo "$i"
../src/test/script/launchers/test_lex "$i"
done


#!/bin/sh
#Le deuxiéme fichier ne retourne pas une erreur pour test_lex
for i in ../src/test/deca/syntax/invalid/provided/*.deca
do
echo "$i"
../src/test/script/launchers/test_lex "$i"
done


