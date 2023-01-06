lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

/* // Deca lexer rules.
DUMMY_TOKEN: .; // A FAIRE : Règle bidon qui reconnait tous les caractères.
                // A FAIRE : Il faut la supprimer et la remplacer par les vraies règles.
 */

/* les unités lexicals */
//mots réservés
ASM : 'asm'
;
CLASS : 'class'
;
EXTENDS : 'extends'
;
ELSE : 'else'
;
FALSE : 'false'
;
IF : 'if'
;
INSTANCEOF : 'instanceof'
;
NEW : 'new'
;
NULL : 'null'
;
READINT : 'readint'
;
READFLOAT : 'readfloat'
;
PRINT : 'print'
;
PRINTLN : 'println'
;
PRINTLNX : 'printlnx'
;
PRINTX : 'printx'
;
PROTECTED : 'protected'
;
RETURN : 'return'
;
THIS : 'this'
;
TRUE : 'true'
;
WHILE : 'while'
;

//les identificateurs
fragment LETTER : ('a'  ..  'z' | 'A' .. 'Z')
;
fragment DIGIT : '0' .. '9'
;
IDENT : (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*
;

//symboles spéciaux 
LESSER : '<'
;
GREATTER : '>'
;
ASSIN: '='
;
PLUS : '+'
;
MINUS : '-'
;
ASTERIK : '*'
;
SLASH : '/'
;
PERCENTAGE : '%'
;
DOT : '.'
;
COMMA : ','
;
L_PARENTHESE : '('
;
R_PARENTHESE : ')'
;
LEFT_CURLYBRACKET : '{'
;
RIGHT_CURLYBRACKET : '}'
;
SUPRISE_MARK : '!'
;
SEMICOLON : ';'
;
EQUAL : '=='
;
NOT_EQUAL : '!='
;
GREATTER_OR_EQUAL : '>='
;
LESSER_OR_EQUAL : '<='
;
AND : '&&'
;
OR : '||'
;

//littéraux entiers
fragment POSITIVE_DIGIT: ('1' .. '9')
;
INT : ('0' | POSITIVE_DIGIT (DIGIT)*)
;

//flottants (décimaux et hexadécimaux)  Littéraux flottants
fragment NUM : (DIGIT)+
;
fragment SIGN : ('+' | '-' | )
;
fragment EXP : ('E' | 'e') SIGN NUM
;
fragment DEC : NUM '.' NUM
;
fragment FLOATDEC : (DEC | (DEC EXP)) ('F' | 'f'| )
;
fragment DIGITHEX : (('A'..'F')|('a'..'f')|('0' .. '9'))
;
fragment NUMHEX : (DIGITHEX)+
;
fragment FLOATHEX : ('0x'|'0X')NUMHEX '.' NUMHEX ('P'|'p') SIGN NUM ('F'|'f'| ) //('0x'|'0X') NUMHEX '.' NUMHEX ('P'|'p') SIGN NUM ('F'|'f')*
;
FLOAT : (FLOATDEC| FLOATHEX)
;



//chaînes de caractères 
fragment STRING_CAR : ~('"'|'\\')
;

//


//Séparateurs
WS : (' ' | '\t' | '\r' | '\n'){
                                 skip();
                              }
;

