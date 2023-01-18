#!/bin/bash

cd "$(dirname "$0")"/.. || exit 1

rm src/test/deca/context/valid/result/*.lis
rm src/test/deca/context/invalid/result/*.lis
rm src/test/deca/syntax/valid/result/*.lis
rm src/test/deca/syntax/invalid/result/*.lis
