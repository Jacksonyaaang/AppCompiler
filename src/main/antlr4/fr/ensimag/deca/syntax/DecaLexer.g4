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

OBRACE : '{';
CBRACE : '}';
OPARENT : '(';
CPARENT : ')';
SEMI : ';';
COMMA : ',';
PRINT : 'print';
PRINTLN : 'println';
PRINTX : 'printx';
PRINTLNX : 'printlnx';
WHILE : 'while';
RETURN : 'return';
IF : 'if';
ELSE : 'else';
AND : '&&';
OR : '||';
EQEQ : '==';
NEQ : '!=';
GEQ : '>=';
LEQ : '<=';
GT : '>';
LT : '<';
INSTANCEOF: 'instaceof';
PLUS : '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
EQUALS : '=';
PERCENT : '%';
EXCLAM : '!';
DOT : '.';
READINT : 'readInt';
READFLOAT : 'readFloat';
NEW : 'new';
TRUE : 'true';
FALSE : 'false';
THIS : 'this';
NULL : 'null';
CLASS : 'class';
EXTENDS : 'extends';
PROTECTED : 'protected';
ASM : 'asm';



fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT : '0'..'9';
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*; // Exception : les mots réservés ne sont pas des identificateurs. A FAIRE


fragment POSITIVE_DIGIT : '1'..'9';
INT : '0' | POSITIVE_DIGIT DIGIT*; 
// Une erreur de compilation est levée si un littéral
// entier n’est pas codable comme un entier signé positif sur 32 bits A FAIRE 

fragment SIGN : '+' | '-' | ; 
fragment NUM : DIGIT+;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f' | ); 
fragment DIGITHEX : '0'..'9' | 'A'..'F'|'a'..'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' |  ); 
FLOAT : FLOATDEC | FLOATHEX;

//Erreur pris du poly
//Les littéraux flottants sont convertis en arrondissant si besoin au flottant IEEE-754 simple précision
//le plus proche. Une erreur de compilation est levée si un littéral est trop grand et que l’arrondi se fait
//vers l’infini, ou bien qu’un littéral non nul est trop petit et que l’arrondi se fait vers zéro. A FAIRE

fragment EOL : '\n'; 
fragment STRING_CAR : ~('"' | '\\' | '\n');
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"';

fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"';

//A FAIRE copied from the slide 36 Analyse lexicale
// L'inclusion de fichier est traitée en analyse lexicale.
// Utiliser la méthode doInclude de AbstractDecaLexer.java.


COMMENTAIRESURUNELIGNE : '//' ~[\n\r]* '\n'  {skip();};
COMMENTAIREMULTILIGNE : '/*' .*? '*/'  {skip();};
WS : (' '
   |'\t'
   |'\n'
   |'\r'
   )+ {skip();};

// COMMENTAIRESURUNELIGNE : '//' ~[\n\r]* '\n'  ->skip;
// COMMENTAIREMULTILIGNE : '/*' .*? '*/'  ->skip;
// WS : (' '
//    |'\t'
//    |'\n'
//    |'\r'
//    )+ ->skip;
   
