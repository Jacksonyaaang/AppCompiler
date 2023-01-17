
#!/bin/sh
for i in ../src/test/deca/syntax/invalid/*.deca
do
echo "$i"
echo  decac "$i"
decac "$i"
done

for i in ../src/test/deca/syntax/invalid/provided/*.deca
do
echo "$i"
echo  decac "$i"
decac "$i"
done


