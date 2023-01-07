#!/bin/sh
echo "----test---"
../../src/test/script/launchers/test_lex "$1"
../../src/test/script/launchers/test_synt "$1"
../../src/test/script/launchers/test_gen "$1"

#../../src/main/bin/decac "$1"

