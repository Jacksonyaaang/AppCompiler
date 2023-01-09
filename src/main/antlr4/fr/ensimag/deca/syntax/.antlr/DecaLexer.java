// Generated from /home/frikha/ensimag/GL/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OBRACE=1, CBRACE=2, OPARENT=3, CPARENT=4, SEMI=5, COMMA=6, PRINT=7, PRINTLN=8, 
		PRINTX=9, PRINTLNX=10, WHILE=11, RETURN=12, IF=13, ELSE=14, AND=15, OR=16, 
		EQEQ=17, NEQ=18, GEQ=19, LEQ=20, GT=21, LT=22, INSTANCEOF=23, PLUS=24, 
		MINUS=25, TIMES=26, SLASH=27, EQUALS=28, PERCENT=29, EXCLAM=30, DOT=31, 
		READINT=32, READFLOAT=33, NEW=34, TRUE=35, FALSE=36, THIS=37, NULL=38, 
		CLASS=39, EXTENDS=40, PROTECTED=41, ASM=42, IDENT=43, INT=44, FLOAT=45, 
		STRING=46, MULTI_LINE_STRING=47, INCLUDE=48, COMMENTAIRESURUNELIGNE=49, 
		COMMENTAIREMULTILIGNE=50, WS=51;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"OBRACE", "CBRACE", "OPARENT", "CPARENT", "SEMI", "COMMA", "PRINT", "PRINTLN", 
			"PRINTX", "PRINTLNX", "WHILE", "RETURN", "IF", "ELSE", "AND", "OR", "EQEQ", 
			"NEQ", "GEQ", "LEQ", "GT", "LT", "INSTANCEOF", "PLUS", "MINUS", "TIMES", 
			"SLASH", "EQUALS", "PERCENT", "EXCLAM", "DOT", "READINT", "READFLOAT", 
			"NEW", "TRUE", "FALSE", "THIS", "NULL", "CLASS", "EXTENDS", "PROTECTED", 
			"ASM", "LETTER", "DIGIT", "IDENT", "POSITIVE_DIGIT", "INT", "SIGN", "NUM", 
			"EXP", "DEC", "FLOATDEC", "DIGITHEX", "NUMHEX", "FLOATHEX", "FLOAT", 
			"EOL", "STRING_CAR", "STRING", "MULTI_LINE_STRING", "FILENAME", "INCLUDE", 
			"COMMENTAIRESURUNELIGNE", "COMMENTAIREMULTILIGNE", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'('", "')'", "';'", "','", "'print'", "'println'", 
			"'printx'", "'printlnx'", "'while'", "'return'", "'if'", "'else'", "'&&'", 
			"'||'", "'=='", "'!='", "'>='", "'<='", "'>'", "'<'", "'instaceof'", 
			"'+'", "'-'", "'*'", "'/'", "'='", "'%'", "'!'", "'.'", "'readInt'", 
			"'readFloat'", "'new'", "'true'", "'false'", "'this'", "'null'", "'class'", 
			"'extends'", "'protected'", "'asm'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OBRACE", "CBRACE", "OPARENT", "CPARENT", "SEMI", "COMMA", "PRINT", 
			"PRINTLN", "PRINTX", "PRINTLNX", "WHILE", "RETURN", "IF", "ELSE", "AND", 
			"OR", "EQEQ", "NEQ", "GEQ", "LEQ", "GT", "LT", "INSTANCEOF", "PLUS", 
			"MINUS", "TIMES", "SLASH", "EQUALS", "PERCENT", "EXCLAM", "DOT", "READINT", 
			"READFLOAT", "NEW", "TRUE", "FALSE", "THIS", "NULL", "CLASS", "EXTENDS", 
			"PROTECTED", "ASM", "IDENT", "INT", "FLOAT", "STRING", "MULTI_LINE_STRING", 
			"INCLUDE", "COMMENTAIRESURUNELIGNE", "COMMENTAIREMULTILIGNE", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 62:
			COMMENTAIRESURUNELIGNE_action((RuleContext)_localctx, actionIndex);
			break;
		case 63:
			COMMENTAIREMULTILIGNE_action((RuleContext)_localctx, actionIndex);
			break;
		case 64:
			WS_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void COMMENTAIRESURUNELIGNE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			skip();
			break;
		}
	}
	private void COMMENTAIREMULTILIGNE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			skip();
			break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			skip();
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\65\u01e7\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3"+
		"\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3"+
		"\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3!\3!\3!\3!\3"+
		"!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3$\3"+
		"$\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3("+
		"\3(\3)\3)\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+"+
		"\3,\3,\3-\3-\3.\3.\5.\u0142\n.\3.\3.\3.\7.\u0147\n.\f.\16.\u014a\13.\3"+
		"/\3/\3\60\3\60\3\60\7\60\u0151\n\60\f\60\16\60\u0154\13\60\5\60\u0156"+
		"\n\60\3\61\3\61\5\61\u015a\n\61\3\62\6\62\u015d\n\62\r\62\16\62\u015e"+
		"\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\5\65\u016d"+
		"\n\65\3\65\3\65\5\65\u0171\n\65\3\66\3\66\3\67\6\67\u0176\n\67\r\67\16"+
		"\67\u0177\38\38\38\38\58\u017e\n8\38\38\38\38\38\38\38\38\58\u0188\n8"+
		"\39\39\59\u018c\n9\3:\3:\3;\3;\3<\3<\3<\3<\3<\3<\7<\u0198\n<\f<\16<\u019b"+
		"\13<\3<\3<\3=\3=\3=\3=\3=\3=\3=\7=\u01a6\n=\f=\16=\u01a9\13=\3=\3=\3>"+
		"\3>\3>\6>\u01b0\n>\r>\16>\u01b1\3?\3?\3?\3?\3?\3?\3?\3?\3?\3?\7?\u01be"+
		"\n?\f?\16?\u01c1\13?\3?\3?\3?\3?\3@\3@\3@\3@\7@\u01cb\n@\f@\16@\u01ce"+
		"\13@\3@\3@\3@\3A\3A\3A\3A\7A\u01d7\nA\fA\16A\u01da\13A\3A\3A\3A\3A\3A"+
		"\3B\6B\u01e2\nB\rB\16B\u01e3\3B\3B\3\u01d8\2C\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26"+
		"+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S"+
		"+U,W\2Y\2[-]\2_.a\2c\2e\2g\2i\2k\2m\2o\2q/s\2u\2w\60y\61{\2}\62\177\63"+
		"\u0081\64\u0083\65\3\2\r\4\2C\\c|\4\2&&aa\4\2--//\4\2GGgg\4\2HHhh\5\2"+
		"\62;CHch\4\2RRrr\5\2\f\f$$^^\4\2/\60aa\4\2\f\f\17\17\5\2\13\f\17\17\""+
		"\"\2\u01f4\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2"+
		"\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S"+
		"\3\2\2\2\2U\3\2\2\2\2[\3\2\2\2\2_\3\2\2\2\2q\3\2\2\2\2w\3\2\2\2\2y\3\2"+
		"\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\3\u0085"+
		"\3\2\2\2\5\u0087\3\2\2\2\7\u0089\3\2\2\2\t\u008b\3\2\2\2\13\u008d\3\2"+
		"\2\2\r\u008f\3\2\2\2\17\u0091\3\2\2\2\21\u0097\3\2\2\2\23\u009f\3\2\2"+
		"\2\25\u00a6\3\2\2\2\27\u00af\3\2\2\2\31\u00b5\3\2\2\2\33\u00bc\3\2\2\2"+
		"\35\u00bf\3\2\2\2\37\u00c4\3\2\2\2!\u00c7\3\2\2\2#\u00ca\3\2\2\2%\u00cd"+
		"\3\2\2\2\'\u00d0\3\2\2\2)\u00d3\3\2\2\2+\u00d6\3\2\2\2-\u00d8\3\2\2\2"+
		"/\u00da\3\2\2\2\61\u00e4\3\2\2\2\63\u00e6\3\2\2\2\65\u00e8\3\2\2\2\67"+
		"\u00ea\3\2\2\29\u00ec\3\2\2\2;\u00ee\3\2\2\2=\u00f0\3\2\2\2?\u00f2\3\2"+
		"\2\2A\u00f4\3\2\2\2C\u00fc\3\2\2\2E\u0106\3\2\2\2G\u010a\3\2\2\2I\u010f"+
		"\3\2\2\2K\u0115\3\2\2\2M\u011a\3\2\2\2O\u011f\3\2\2\2Q\u0125\3\2\2\2S"+
		"\u012d\3\2\2\2U\u0137\3\2\2\2W\u013b\3\2\2\2Y\u013d\3\2\2\2[\u0141\3\2"+
		"\2\2]\u014b\3\2\2\2_\u0155\3\2\2\2a\u0159\3\2\2\2c\u015c\3\2\2\2e\u0160"+
		"\3\2\2\2g\u0164\3\2\2\2i\u016c\3\2\2\2k\u0172\3\2\2\2m\u0175\3\2\2\2o"+
		"\u017d\3\2\2\2q\u018b\3\2\2\2s\u018d\3\2\2\2u\u018f\3\2\2\2w\u0191\3\2"+
		"\2\2y\u019e\3\2\2\2{\u01af\3\2\2\2}\u01b3\3\2\2\2\177\u01c6\3\2\2\2\u0081"+
		"\u01d2\3\2\2\2\u0083\u01e1\3\2\2\2\u0085\u0086\7}\2\2\u0086\4\3\2\2\2"+
		"\u0087\u0088\7\177\2\2\u0088\6\3\2\2\2\u0089\u008a\7*\2\2\u008a\b\3\2"+
		"\2\2\u008b\u008c\7+\2\2\u008c\n\3\2\2\2\u008d\u008e\7=\2\2\u008e\f\3\2"+
		"\2\2\u008f\u0090\7.\2\2\u0090\16\3\2\2\2\u0091\u0092\7r\2\2\u0092\u0093"+
		"\7t\2\2\u0093\u0094\7k\2\2\u0094\u0095\7p\2\2\u0095\u0096\7v\2\2\u0096"+
		"\20\3\2\2\2\u0097\u0098\7r\2\2\u0098\u0099\7t\2\2\u0099\u009a\7k\2\2\u009a"+
		"\u009b\7p\2\2\u009b\u009c\7v\2\2\u009c\u009d\7n\2\2\u009d\u009e\7p\2\2"+
		"\u009e\22\3\2\2\2\u009f\u00a0\7r\2\2\u00a0\u00a1\7t\2\2\u00a1\u00a2\7"+
		"k\2\2\u00a2\u00a3\7p\2\2\u00a3\u00a4\7v\2\2\u00a4\u00a5\7z\2\2\u00a5\24"+
		"\3\2\2\2\u00a6\u00a7\7r\2\2\u00a7\u00a8\7t\2\2\u00a8\u00a9\7k\2\2\u00a9"+
		"\u00aa\7p\2\2\u00aa\u00ab\7v\2\2\u00ab\u00ac\7n\2\2\u00ac\u00ad\7p\2\2"+
		"\u00ad\u00ae\7z\2\2\u00ae\26\3\2\2\2\u00af\u00b0\7y\2\2\u00b0\u00b1\7"+
		"j\2\2\u00b1\u00b2\7k\2\2\u00b2\u00b3\7n\2\2\u00b3\u00b4\7g\2\2\u00b4\30"+
		"\3\2\2\2\u00b5\u00b6\7t\2\2\u00b6\u00b7\7g\2\2\u00b7\u00b8\7v\2\2\u00b8"+
		"\u00b9\7w\2\2\u00b9\u00ba\7t\2\2\u00ba\u00bb\7p\2\2\u00bb\32\3\2\2\2\u00bc"+
		"\u00bd\7k\2\2\u00bd\u00be\7h\2\2\u00be\34\3\2\2\2\u00bf\u00c0\7g\2\2\u00c0"+
		"\u00c1\7n\2\2\u00c1\u00c2\7u\2\2\u00c2\u00c3\7g\2\2\u00c3\36\3\2\2\2\u00c4"+
		"\u00c5\7(\2\2\u00c5\u00c6\7(\2\2\u00c6 \3\2\2\2\u00c7\u00c8\7~\2\2\u00c8"+
		"\u00c9\7~\2\2\u00c9\"\3\2\2\2\u00ca\u00cb\7?\2\2\u00cb\u00cc\7?\2\2\u00cc"+
		"$\3\2\2\2\u00cd\u00ce\7#\2\2\u00ce\u00cf\7?\2\2\u00cf&\3\2\2\2\u00d0\u00d1"+
		"\7@\2\2\u00d1\u00d2\7?\2\2\u00d2(\3\2\2\2\u00d3\u00d4\7>\2\2\u00d4\u00d5"+
		"\7?\2\2\u00d5*\3\2\2\2\u00d6\u00d7\7@\2\2\u00d7,\3\2\2\2\u00d8\u00d9\7"+
		">\2\2\u00d9.\3\2\2\2\u00da\u00db\7k\2\2\u00db\u00dc\7p\2\2\u00dc\u00dd"+
		"\7u\2\2\u00dd\u00de\7v\2\2\u00de\u00df\7c\2\2\u00df\u00e0\7e\2\2\u00e0"+
		"\u00e1\7g\2\2\u00e1\u00e2\7q\2\2\u00e2\u00e3\7h\2\2\u00e3\60\3\2\2\2\u00e4"+
		"\u00e5\7-\2\2\u00e5\62\3\2\2\2\u00e6\u00e7\7/\2\2\u00e7\64\3\2\2\2\u00e8"+
		"\u00e9\7,\2\2\u00e9\66\3\2\2\2\u00ea\u00eb\7\61\2\2\u00eb8\3\2\2\2\u00ec"+
		"\u00ed\7?\2\2\u00ed:\3\2\2\2\u00ee\u00ef\7\'\2\2\u00ef<\3\2\2\2\u00f0"+
		"\u00f1\7#\2\2\u00f1>\3\2\2\2\u00f2\u00f3\7\60\2\2\u00f3@\3\2\2\2\u00f4"+
		"\u00f5\7t\2\2\u00f5\u00f6\7g\2\2\u00f6\u00f7\7c\2\2\u00f7\u00f8\7f\2\2"+
		"\u00f8\u00f9\7K\2\2\u00f9\u00fa\7p\2\2\u00fa\u00fb\7v\2\2\u00fbB\3\2\2"+
		"\2\u00fc\u00fd\7t\2\2\u00fd\u00fe\7g\2\2\u00fe\u00ff\7c\2\2\u00ff\u0100"+
		"\7f\2\2\u0100\u0101\7H\2\2\u0101\u0102\7n\2\2\u0102\u0103\7q\2\2\u0103"+
		"\u0104\7c\2\2\u0104\u0105\7v\2\2\u0105D\3\2\2\2\u0106\u0107\7p\2\2\u0107"+
		"\u0108\7g\2\2\u0108\u0109\7y\2\2\u0109F\3\2\2\2\u010a\u010b\7v\2\2\u010b"+
		"\u010c\7t\2\2\u010c\u010d\7w\2\2\u010d\u010e\7g\2\2\u010eH\3\2\2\2\u010f"+
		"\u0110\7h\2\2\u0110\u0111\7c\2\2\u0111\u0112\7n\2\2\u0112\u0113\7u\2\2"+
		"\u0113\u0114\7g\2\2\u0114J\3\2\2\2\u0115\u0116\7v\2\2\u0116\u0117\7j\2"+
		"\2\u0117\u0118\7k\2\2\u0118\u0119\7u\2\2\u0119L\3\2\2\2\u011a\u011b\7"+
		"p\2\2\u011b\u011c\7w\2\2\u011c\u011d\7n\2\2\u011d\u011e\7n\2\2\u011eN"+
		"\3\2\2\2\u011f\u0120\7e\2\2\u0120\u0121\7n\2\2\u0121\u0122\7c\2\2\u0122"+
		"\u0123\7u\2\2\u0123\u0124\7u\2\2\u0124P\3\2\2\2\u0125\u0126\7g\2\2\u0126"+
		"\u0127\7z\2\2\u0127\u0128\7v\2\2\u0128\u0129\7g\2\2\u0129\u012a\7p\2\2"+
		"\u012a\u012b\7f\2\2\u012b\u012c\7u\2\2\u012cR\3\2\2\2\u012d\u012e\7r\2"+
		"\2\u012e\u012f\7t\2\2\u012f\u0130\7q\2\2\u0130\u0131\7v\2\2\u0131\u0132"+
		"\7g\2\2\u0132\u0133\7e\2\2\u0133\u0134\7v\2\2\u0134\u0135\7g\2\2\u0135"+
		"\u0136\7f\2\2\u0136T\3\2\2\2\u0137\u0138\7c\2\2\u0138\u0139\7u\2\2\u0139"+
		"\u013a\7o\2\2\u013aV\3\2\2\2\u013b\u013c\t\2\2\2\u013cX\3\2\2\2\u013d"+
		"\u013e\4\62;\2\u013eZ\3\2\2\2\u013f\u0142\5W,\2\u0140\u0142\t\3\2\2\u0141"+
		"\u013f\3\2\2\2\u0141\u0140\3\2\2\2\u0142\u0148\3\2\2\2\u0143\u0147\5W"+
		",\2\u0144\u0147\5Y-\2\u0145\u0147\t\3\2\2\u0146\u0143\3\2\2\2\u0146\u0144"+
		"\3\2\2\2\u0146\u0145\3\2\2\2\u0147\u014a\3\2\2\2\u0148\u0146\3\2\2\2\u0148"+
		"\u0149\3\2\2\2\u0149\\\3\2\2\2\u014a\u0148\3\2\2\2\u014b\u014c\4\63;\2"+
		"\u014c^\3\2\2\2\u014d\u0156\7\62\2\2\u014e\u0152\5]/\2\u014f\u0151\5Y"+
		"-\2\u0150\u014f\3\2\2\2\u0151\u0154\3\2\2\2\u0152\u0150\3\2\2\2\u0152"+
		"\u0153\3\2\2\2\u0153\u0156\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u014d\3\2"+
		"\2\2\u0155\u014e\3\2\2\2\u0156`\3\2\2\2\u0157\u015a\t\4\2\2\u0158\u015a"+
		"\3\2\2\2\u0159\u0157\3\2\2\2\u0159\u0158\3\2\2\2\u015ab\3\2\2\2\u015b"+
		"\u015d\5Y-\2\u015c\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u015c\3\2\2"+
		"\2\u015e\u015f\3\2\2\2\u015fd\3\2\2\2\u0160\u0161\t\5\2\2\u0161\u0162"+
		"\5a\61\2\u0162\u0163\5c\62\2\u0163f\3\2\2\2\u0164\u0165\5c\62\2\u0165"+
		"\u0166\7\60\2\2\u0166\u0167\5c\62\2\u0167h\3\2\2\2\u0168\u016d\5g\64\2"+
		"\u0169\u016a\5g\64\2\u016a\u016b\5e\63\2\u016b\u016d\3\2\2\2\u016c\u0168"+
		"\3\2\2\2\u016c\u0169\3\2\2\2\u016d\u0170\3\2\2\2\u016e\u0171\t\6\2\2\u016f"+
		"\u0171\3\2\2\2\u0170\u016e\3\2\2\2\u0170\u016f\3\2\2\2\u0171j\3\2\2\2"+
		"\u0172\u0173\t\7\2\2\u0173l\3\2\2\2\u0174\u0176\5k\66\2\u0175\u0174\3"+
		"\2\2\2\u0176\u0177\3\2\2\2\u0177\u0175\3\2\2\2\u0177\u0178\3\2\2\2\u0178"+
		"n\3\2\2\2\u0179\u017a\7\62\2\2\u017a\u017e\7z\2\2\u017b\u017c\7\62\2\2"+
		"\u017c\u017e\7Z\2\2\u017d\u0179\3\2\2\2\u017d\u017b\3\2\2\2\u017e\u017f"+
		"\3\2\2\2\u017f\u0180\5m\67\2\u0180\u0181\7\60\2\2\u0181\u0182\5m\67\2"+
		"\u0182\u0183\t\b\2\2\u0183\u0184\5a\61\2\u0184\u0187\5c\62\2\u0185\u0188"+
		"\t\6\2\2\u0186\u0188\3\2\2\2\u0187\u0185\3\2\2\2\u0187\u0186\3\2\2\2\u0188"+
		"p\3\2\2\2\u0189\u018c\5i\65\2\u018a\u018c\5o8\2\u018b\u0189\3\2\2\2\u018b"+
		"\u018a\3\2\2\2\u018cr\3\2\2\2\u018d\u018e\7\f\2\2\u018et\3\2\2\2\u018f"+
		"\u0190\n\t\2\2\u0190v\3\2\2\2\u0191\u0199\7$\2\2\u0192\u0198\5u;\2\u0193"+
		"\u0194\7^\2\2\u0194\u0198\7$\2\2\u0195\u0196\7^\2\2\u0196\u0198\7^\2\2"+
		"\u0197\u0192\3\2\2\2\u0197\u0193\3\2\2\2\u0197\u0195\3\2\2\2\u0198\u019b"+
		"\3\2\2\2\u0199\u0197\3\2\2\2\u0199\u019a\3\2\2\2\u019a\u019c\3\2\2\2\u019b"+
		"\u0199\3\2\2\2\u019c\u019d\7$\2\2\u019dx\3\2\2\2\u019e\u01a7\7$\2\2\u019f"+
		"\u01a6\5u;\2\u01a0\u01a6\5s:\2\u01a1\u01a2\7^\2\2\u01a2\u01a6\7$\2\2\u01a3"+
		"\u01a4\7^\2\2\u01a4\u01a6\7^\2\2\u01a5\u019f\3\2\2\2\u01a5\u01a0\3\2\2"+
		"\2\u01a5\u01a1\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6\u01a9\3\2\2\2\u01a7\u01a5"+
		"\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01aa\3\2\2\2\u01a9\u01a7\3\2\2\2\u01aa"+
		"\u01ab\7$\2\2\u01abz\3\2\2\2\u01ac\u01b0\5W,\2\u01ad\u01b0\5Y-\2\u01ae"+
		"\u01b0\t\n\2\2\u01af\u01ac\3\2\2\2\u01af\u01ad\3\2\2\2\u01af\u01ae\3\2"+
		"\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01af\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2"+
		"|\3\2\2\2\u01b3\u01b4\7%\2\2\u01b4\u01b5\7k\2\2\u01b5\u01b6\7p\2\2\u01b6"+
		"\u01b7\7e\2\2\u01b7\u01b8\7n\2\2\u01b8\u01b9\7w\2\2\u01b9\u01ba\7f\2\2"+
		"\u01ba\u01bb\7g\2\2\u01bb\u01bf\3\2\2\2\u01bc\u01be\7\"\2\2\u01bd\u01bc"+
		"\3\2\2\2\u01be\u01c1\3\2\2\2\u01bf\u01bd\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0"+
		"\u01c2\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c2\u01c3\7$\2\2\u01c3\u01c4\5{>"+
		"\2\u01c4\u01c5\7$\2\2\u01c5~\3\2\2\2\u01c6\u01c7\7\61\2\2\u01c7\u01c8"+
		"\7\61\2\2\u01c8\u01cc\3\2\2\2\u01c9\u01cb\n\13\2\2\u01ca\u01c9\3\2\2\2"+
		"\u01cb\u01ce\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cd\3\2\2\2\u01cd\u01cf"+
		"\3\2\2\2\u01ce\u01cc\3\2\2\2\u01cf\u01d0\7\f\2\2\u01d0\u01d1\b@\2\2\u01d1"+
		"\u0080\3\2\2\2\u01d2\u01d3\7\61\2\2\u01d3\u01d4\7,\2\2\u01d4\u01d8\3\2"+
		"\2\2\u01d5\u01d7\13\2\2\2\u01d6\u01d5\3\2\2\2\u01d7\u01da\3\2\2\2\u01d8"+
		"\u01d9\3\2\2\2\u01d8\u01d6\3\2\2\2\u01d9\u01db\3\2\2\2\u01da\u01d8\3\2"+
		"\2\2\u01db\u01dc\7,\2\2\u01dc\u01dd\7\61\2\2\u01dd\u01de\3\2\2\2\u01de"+
		"\u01df\bA\3\2\u01df\u0082\3\2\2\2\u01e0\u01e2\t\f\2\2\u01e1\u01e0\3\2"+
		"\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e3\u01e4\3\2\2\2\u01e4"+
		"\u01e5\3\2\2\2\u01e5\u01e6\bB\4\2\u01e6\u0084\3\2\2\2\32\2\u0141\u0146"+
		"\u0148\u0152\u0155\u0159\u015e\u016c\u0170\u0177\u017d\u0187\u018b\u0197"+
		"\u0199\u01a5\u01a7\u01af\u01b1\u01bf\u01cc\u01d8\u01e3\5\3@\2\3A\3\3B"+
		"\4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}