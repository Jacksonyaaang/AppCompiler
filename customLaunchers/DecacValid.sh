#!/bin/sh
for i in ../src/test/deca/syntax/valid/provided/*.deca
do
echo "$i"
../src/main/bin/decac "$i"
done

for i in ../src/test/deca/codegen/valid/provided/*.deca
do
echo "$i"
../src/main/bin/decac "$i"
done

for i in ../src/test/deca/context/valid/provided/*.deca
do
echo "$i"
../src/main/bin/decac "$i"
done

