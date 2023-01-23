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
		OBRACE=1, CBRACE=2, OPARENT=3, CPARENT=4, OBRACKET=5, CBRACKET=6, SEMI=7, 
		COMMA=8, PRINT=9, PRINTLN=10, PRINTX=11, PRINTLNX=12, WHILE=13, RETURN=14, 
		IF=15, ELSE=16, AND=17, OR=18, EQEQ=19, NEQ=20, GEQ=21, LEQ=22, GT=23, 
		LT=24, INSTANCEOF=25, PLUS=26, MINUS=27, TIMES=28, SLASH=29, EQUALS=30, 
		PERCENT=31, EXCLAM=32, DOT=33, READINT=34, READFLOAT=35, NEW=36, TRUE=37, 
		FALSE=38, THIS=39, NULL=40, CLASS=41, EXTENDS=42, PROTECTED=43, ASM=44, 
		IDENT=45, INT=46, FLOAT=47, STRING=48, MULTI_LINE_STRING=49, INCLUDE=50, 
		COMMENTAIRESURUNELIGNE=51, COMMENTAIREMULTILIGNE=52, WS=53;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"OBRACE", "CBRACE", "OPARENT", "CPARENT", "OBRACKET", "CBRACKET", "SEMI", 
			"COMMA", "PRINT", "PRINTLN", "PRINTX", "PRINTLNX", "WHILE", "RETURN", 
			"IF", "ELSE", "AND", "OR", "EQEQ", "NEQ", "GEQ", "LEQ", "GT", "LT", "INSTANCEOF", 
			"PLUS", "MINUS", "TIMES", "SLASH", "EQUALS", "PERCENT", "EXCLAM", "DOT", 
			"READINT", "READFLOAT", "NEW", "TRUE", "FALSE", "THIS", "NULL", "CLASS", 
			"EXTENDS", "PROTECTED", "ASM", "LETTER", "DIGIT", "IDENT", "POSITIVE_DIGIT", 
			"INT", "SIGN", "NUM", "EXP", "DEC", "FLOATDEC", "DIGITHEX", "NUMHEX", 
			"FLOATHEX", "FLOAT", "EOL", "STRING_CAR", "STRING", "MULTI_LINE_STRING", 
			"FILENAME", "INCLUDE", "COMMENTAIRESURUNELIGNE", "COMMENTAIREMULTILIGNE", 
			"WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'('", "')'", "'['", "']'", "';'", "','", "'print'", 
			"'println'", "'printx'", "'printlnx'", "'while'", "'return'", "'if'", 
			"'else'", "'&&'", "'||'", "'=='", "'!='", "'>='", "'<='", "'>'", "'<'", 
			"'instanceof'", "'+'", "'-'", "'*'", "'/'", "'='", "'%'", "'!'", "'.'", 
			"'readInt'", "'readFloat'", "'new'", "'true'", "'false'", "'this'", "'null'", 
			"'class'", "'extends'", "'protected'", "'asm'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OBRACE", "CBRACE", "OPARENT", "CPARENT", "OBRACKET", "CBRACKET", 
			"SEMI", "COMMA", "PRINT", "PRINTLN", "PRINTX", "PRINTLNX", "WHILE", "RETURN", 
			"IF", "ELSE", "AND", "OR", "EQEQ", "NEQ", "GEQ", "LEQ", "GT", "LT", "INSTANCEOF", 
			"PLUS", "MINUS", "TIMES", "SLASH", "EQUALS", "PERCENT", "EXCLAM", "DOT", 
			"READINT", "READFLOAT", "NEW", "TRUE", "FALSE", "THIS", "NULL", "CLASS", 
			"EXTENDS", "PROTECTED", "ASM", "IDENT", "INT", "FLOAT", "STRING", "MULTI_LINE_STRING", 
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
		case 63:
			INCLUDE_action((RuleContext)_localctx, actionIndex);
			break;
		case 64:
			COMMENTAIRESURUNELIGNE_action((RuleContext)_localctx, actionIndex);
			break;
		case 65:
			COMMENTAIREMULTILIGNE_action((RuleContext)_localctx, actionIndex);
			break;
		case 66:
			WS_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void INCLUDE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			   super.doInclude(getText());
			   
			break;
		}
	}
	private void COMMENTAIRESURUNELIGNE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			skip();
			break;
		}
	}
	private void COMMENTAIREMULTILIGNE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			skip();
			break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			skip();
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\67\u01f1\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\3\2\3\2\3\3\3\3\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3"+
		"\27\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3"+
		" \3 \3!\3!\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3$"+
		"\3$\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3("+
		"\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,"+
		"\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3.\3.\3/\3/\3\60\3\60\5\60\u014b\n\60\3"+
		"\60\3\60\3\60\7\60\u0150\n\60\f\60\16\60\u0153\13\60\3\61\3\61\3\62\3"+
		"\62\3\62\7\62\u015a\n\62\f\62\16\62\u015d\13\62\5\62\u015f\n\62\3\63\3"+
		"\63\5\63\u0163\n\63\3\64\6\64\u0166\n\64\r\64\16\64\u0167\3\65\3\65\3"+
		"\65\3\65\3\66\3\66\3\66\3\66\3\67\3\67\3\67\3\67\5\67\u0176\n\67\3\67"+
		"\3\67\5\67\u017a\n\67\38\38\39\69\u017f\n9\r9\169\u0180\3:\3:\3:\3:\5"+
		":\u0187\n:\3:\3:\3:\3:\3:\3:\3:\3:\5:\u0191\n:\3;\3;\5;\u0195\n;\3<\3"+
		"<\3=\3=\3>\3>\3>\3>\3>\3>\7>\u01a1\n>\f>\16>\u01a4\13>\3>\3>\3?\3?\3?"+
		"\3?\3?\3?\3?\7?\u01af\n?\f?\16?\u01b2\13?\3?\3?\3@\3@\3@\6@\u01b9\n@\r"+
		"@\16@\u01ba\3A\3A\3A\3A\3A\3A\3A\3A\3A\3A\7A\u01c7\nA\fA\16A\u01ca\13"+
		"A\3A\3A\3A\3A\3A\3B\3B\3B\3B\7B\u01d5\nB\fB\16B\u01d8\13B\3B\3B\3B\3C"+
		"\3C\3C\3C\7C\u01e1\nC\fC\16C\u01e4\13C\3C\3C\3C\3C\3C\3D\6D\u01ec\nD\r"+
		"D\16D\u01ed\3D\3D\3\u01e2\2E\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13"+
		"\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61"+
		"\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[\2]\2_/"+
		"a\2c\60e\2g\2i\2k\2m\2o\2q\2s\2u\61w\2y\2{\62}\63\177\2\u0081\64\u0083"+
		"\65\u0085\66\u0087\67\3\2\r\4\2C\\c|\4\2&&aa\4\2--//\4\2GGgg\4\2HHhh\5"+
		"\2\62;CHch\4\2RRrr\5\2\f\f$$^^\4\2/\60aa\4\2\f\f\17\17\5\2\13\f\17\17"+
		"\"\"\2\u01fe\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2"+
		"\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2"+
		"\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2"+
		"\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2"+
		"\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3"+
		"\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2"+
		"\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2"+
		"S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2_\3\2\2\2\2c\3\2\2\2\2u\3"+
		"\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3"+
		"\2\2\2\2\u0087\3\2\2\2\3\u0089\3\2\2\2\5\u008b\3\2\2\2\7\u008d\3\2\2\2"+
		"\t\u008f\3\2\2\2\13\u0091\3\2\2\2\r\u0093\3\2\2\2\17\u0095\3\2\2\2\21"+
		"\u0097\3\2\2\2\23\u0099\3\2\2\2\25\u009f\3\2\2\2\27\u00a7\3\2\2\2\31\u00ae"+
		"\3\2\2\2\33\u00b7\3\2\2\2\35\u00bd\3\2\2\2\37\u00c4\3\2\2\2!\u00c7\3\2"+
		"\2\2#\u00cc\3\2\2\2%\u00cf\3\2\2\2\'\u00d2\3\2\2\2)\u00d5\3\2\2\2+\u00d8"+
		"\3\2\2\2-\u00db\3\2\2\2/\u00de\3\2\2\2\61\u00e0\3\2\2\2\63\u00e2\3\2\2"+
		"\2\65\u00ed\3\2\2\2\67\u00ef\3\2\2\29\u00f1\3\2\2\2;\u00f3\3\2\2\2=\u00f5"+
		"\3\2\2\2?\u00f7\3\2\2\2A\u00f9\3\2\2\2C\u00fb\3\2\2\2E\u00fd\3\2\2\2G"+
		"\u0105\3\2\2\2I\u010f\3\2\2\2K\u0113\3\2\2\2M\u0118\3\2\2\2O\u011e\3\2"+
		"\2\2Q\u0123\3\2\2\2S\u0128\3\2\2\2U\u012e\3\2\2\2W\u0136\3\2\2\2Y\u0140"+
		"\3\2\2\2[\u0144\3\2\2\2]\u0146\3\2\2\2_\u014a\3\2\2\2a\u0154\3\2\2\2c"+
		"\u015e\3\2\2\2e\u0162\3\2\2\2g\u0165\3\2\2\2i\u0169\3\2\2\2k\u016d\3\2"+
		"\2\2m\u0175\3\2\2\2o\u017b\3\2\2\2q\u017e\3\2\2\2s\u0186\3\2\2\2u\u0194"+
		"\3\2\2\2w\u0196\3\2\2\2y\u0198\3\2\2\2{\u019a\3\2\2\2}\u01a7\3\2\2\2\177"+
		"\u01b8\3\2\2\2\u0081\u01bc\3\2\2\2\u0083\u01d0\3\2\2\2\u0085\u01dc\3\2"+
		"\2\2\u0087\u01eb\3\2\2\2\u0089\u008a\7}\2\2\u008a\4\3\2\2\2\u008b\u008c"+
		"\7\177\2\2\u008c\6\3\2\2\2\u008d\u008e\7*\2\2\u008e\b\3\2\2\2\u008f\u0090"+
		"\7+\2\2\u0090\n\3\2\2\2\u0091\u0092\7]\2\2\u0092\f\3\2\2\2\u0093\u0094"+
		"\7_\2\2\u0094\16\3\2\2\2\u0095\u0096\7=\2\2\u0096\20\3\2\2\2\u0097\u0098"+
		"\7.\2\2\u0098\22\3\2\2\2\u0099\u009a\7r\2\2\u009a\u009b\7t\2\2\u009b\u009c"+
		"\7k\2\2\u009c\u009d\7p\2\2\u009d\u009e\7v\2\2\u009e\24\3\2\2\2\u009f\u00a0"+
		"\7r\2\2\u00a0\u00a1\7t\2\2\u00a1\u00a2\7k\2\2\u00a2\u00a3\7p\2\2\u00a3"+
		"\u00a4\7v\2\2\u00a4\u00a5\7n\2\2\u00a5\u00a6\7p\2\2\u00a6\26\3\2\2\2\u00a7"+
		"\u00a8\7r\2\2\u00a8\u00a9\7t\2\2\u00a9\u00aa\7k\2\2\u00aa\u00ab\7p\2\2"+
		"\u00ab\u00ac\7v\2\2\u00ac\u00ad\7z\2\2\u00ad\30\3\2\2\2\u00ae\u00af\7"+
		"r\2\2\u00af\u00b0\7t\2\2\u00b0\u00b1\7k\2\2\u00b1\u00b2\7p\2\2\u00b2\u00b3"+
		"\7v\2\2\u00b3\u00b4\7n\2\2\u00b4\u00b5\7p\2\2\u00b5\u00b6\7z\2\2\u00b6"+
		"\32\3\2\2\2\u00b7\u00b8\7y\2\2\u00b8\u00b9\7j\2\2\u00b9\u00ba\7k\2\2\u00ba"+
		"\u00bb\7n\2\2\u00bb\u00bc\7g\2\2\u00bc\34\3\2\2\2\u00bd\u00be\7t\2\2\u00be"+
		"\u00bf\7g\2\2\u00bf\u00c0\7v\2\2\u00c0\u00c1\7w\2\2\u00c1\u00c2\7t\2\2"+
		"\u00c2\u00c3\7p\2\2\u00c3\36\3\2\2\2\u00c4\u00c5\7k\2\2\u00c5\u00c6\7"+
		"h\2\2\u00c6 \3\2\2\2\u00c7\u00c8\7g\2\2\u00c8\u00c9\7n\2\2\u00c9\u00ca"+
		"\7u\2\2\u00ca\u00cb\7g\2\2\u00cb\"\3\2\2\2\u00cc\u00cd\7(\2\2\u00cd\u00ce"+
		"\7(\2\2\u00ce$\3\2\2\2\u00cf\u00d0\7~\2\2\u00d0\u00d1\7~\2\2\u00d1&\3"+
		"\2\2\2\u00d2\u00d3\7?\2\2\u00d3\u00d4\7?\2\2\u00d4(\3\2\2\2\u00d5\u00d6"+
		"\7#\2\2\u00d6\u00d7\7?\2\2\u00d7*\3\2\2\2\u00d8\u00d9\7@\2\2\u00d9\u00da"+
		"\7?\2\2\u00da,\3\2\2\2\u00db\u00dc\7>\2\2\u00dc\u00dd\7?\2\2\u00dd.\3"+
		"\2\2\2\u00de\u00df\7@\2\2\u00df\60\3\2\2\2\u00e0\u00e1\7>\2\2\u00e1\62"+
		"\3\2\2\2\u00e2\u00e3\7k\2\2\u00e3\u00e4\7p\2\2\u00e4\u00e5\7u\2\2\u00e5"+
		"\u00e6\7v\2\2\u00e6\u00e7\7c\2\2\u00e7\u00e8\7p\2\2\u00e8\u00e9\7e\2\2"+
		"\u00e9\u00ea\7g\2\2\u00ea\u00eb\7q\2\2\u00eb\u00ec\7h\2\2\u00ec\64\3\2"+
		"\2\2\u00ed\u00ee\7-\2\2\u00ee\66\3\2\2\2\u00ef\u00f0\7/\2\2\u00f08\3\2"+
		"\2\2\u00f1\u00f2\7,\2\2\u00f2:\3\2\2\2\u00f3\u00f4\7\61\2\2\u00f4<\3\2"+
		"\2\2\u00f5\u00f6\7?\2\2\u00f6>\3\2\2\2\u00f7\u00f8\7\'\2\2\u00f8@\3\2"+
		"\2\2\u00f9\u00fa\7#\2\2\u00faB\3\2\2\2\u00fb\u00fc\7\60\2\2\u00fcD\3\2"+
		"\2\2\u00fd\u00fe\7t\2\2\u00fe\u00ff\7g\2\2\u00ff\u0100\7c\2\2\u0100\u0101"+
		"\7f\2\2\u0101\u0102\7K\2\2\u0102\u0103\7p\2\2\u0103\u0104\7v\2\2\u0104"+
		"F\3\2\2\2\u0105\u0106\7t\2\2\u0106\u0107\7g\2\2\u0107\u0108\7c\2\2\u0108"+
		"\u0109\7f\2\2\u0109\u010a\7H\2\2\u010a\u010b\7n\2\2\u010b\u010c\7q\2\2"+
		"\u010c\u010d\7c\2\2\u010d\u010e\7v\2\2\u010eH\3\2\2\2\u010f\u0110\7p\2"+
		"\2\u0110\u0111\7g\2\2\u0111\u0112\7y\2\2\u0112J\3\2\2\2\u0113\u0114\7"+
		"v\2\2\u0114\u0115\7t\2\2\u0115\u0116\7w\2\2\u0116\u0117\7g\2\2\u0117L"+
		"\3\2\2\2\u0118\u0119\7h\2\2\u0119\u011a\7c\2\2\u011a\u011b\7n\2\2\u011b"+
		"\u011c\7u\2\2\u011c\u011d\7g\2\2\u011dN\3\2\2\2\u011e\u011f\7v\2\2\u011f"+
		"\u0120\7j\2\2\u0120\u0121\7k\2\2\u0121\u0122\7u\2\2\u0122P\3\2\2\2\u0123"+
		"\u0124\7p\2\2\u0124\u0125\7w\2\2\u0125\u0126\7n\2\2\u0126\u0127\7n\2\2"+
		"\u0127R\3\2\2\2\u0128\u0129\7e\2\2\u0129\u012a\7n\2\2\u012a\u012b\7c\2"+
		"\2\u012b\u012c\7u\2\2\u012c\u012d\7u\2\2\u012dT\3\2\2\2\u012e\u012f\7"+
		"g\2\2\u012f\u0130\7z\2\2\u0130\u0131\7v\2\2\u0131\u0132\7g\2\2\u0132\u0133"+
		"\7p\2\2\u0133\u0134\7f\2\2\u0134\u0135\7u\2\2\u0135V\3\2\2\2\u0136\u0137"+
		"\7r\2\2\u0137\u0138\7t\2\2\u0138\u0139\7q\2\2\u0139\u013a\7v\2\2\u013a"+
		"\u013b\7g\2\2\u013b\u013c\7e\2\2\u013c\u013d\7v\2\2\u013d\u013e\7g\2\2"+
		"\u013e\u013f\7f\2\2\u013fX\3\2\2\2\u0140\u0141\7c\2\2\u0141\u0142\7u\2"+
		"\2\u0142\u0143\7o\2\2\u0143Z\3\2\2\2\u0144\u0145\t\2\2\2\u0145\\\3\2\2"+
		"\2\u0146\u0147\4\62;\2\u0147^\3\2\2\2\u0148\u014b\5[.\2\u0149\u014b\t"+
		"\3\2\2\u014a\u0148\3\2\2\2\u014a\u0149\3\2\2\2\u014b\u0151\3\2\2\2\u014c"+
		"\u0150\5[.\2\u014d\u0150\5]/\2\u014e\u0150\t\3\2\2\u014f\u014c\3\2\2\2"+
		"\u014f\u014d\3\2\2\2\u014f\u014e\3\2\2\2\u0150\u0153\3\2\2\2\u0151\u014f"+
		"\3\2\2\2\u0151\u0152\3\2\2\2\u0152`\3\2\2\2\u0153\u0151\3\2\2\2\u0154"+
		"\u0155\4\63;\2\u0155b\3\2\2\2\u0156\u015f\7\62\2\2\u0157\u015b\5a\61\2"+
		"\u0158\u015a\5]/\2\u0159\u0158\3\2\2\2\u015a\u015d\3\2\2\2\u015b\u0159"+
		"\3\2\2\2\u015b\u015c\3\2\2\2\u015c\u015f\3\2\2\2\u015d\u015b\3\2\2\2\u015e"+
		"\u0156\3\2\2\2\u015e\u0157\3\2\2\2\u015fd\3\2\2\2\u0160\u0163\t\4\2\2"+
		"\u0161\u0163\3\2\2\2\u0162\u0160\3\2\2\2\u0162\u0161\3\2\2\2\u0163f\3"+
		"\2\2\2\u0164\u0166\5]/\2\u0165\u0164\3\2\2\2\u0166\u0167\3\2\2\2\u0167"+
		"\u0165\3\2\2\2\u0167\u0168\3\2\2\2\u0168h\3\2\2\2\u0169\u016a\t\5\2\2"+
		"\u016a\u016b\5e\63\2\u016b\u016c\5g\64\2\u016cj\3\2\2\2\u016d\u016e\5"+
		"g\64\2\u016e\u016f\7\60\2\2\u016f\u0170\5g\64\2\u0170l\3\2\2\2\u0171\u0176"+
		"\5k\66\2\u0172\u0173\5k\66\2\u0173\u0174\5i\65\2\u0174\u0176\3\2\2\2\u0175"+
		"\u0171\3\2\2\2\u0175\u0172\3\2\2\2\u0176\u0179\3\2\2\2\u0177\u017a\t\6"+
		"\2\2\u0178\u017a\3\2\2\2\u0179\u0177\3\2\2\2\u0179\u0178\3\2\2\2\u017a"+
		"n\3\2\2\2\u017b\u017c\t\7\2\2\u017cp\3\2\2\2\u017d\u017f\5o8\2\u017e\u017d"+
		"\3\2\2\2\u017f\u0180\3\2\2\2\u0180\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181"+
		"r\3\2\2\2\u0182\u0183\7\62\2\2\u0183\u0187\7z\2\2\u0184\u0185\7\62\2\2"+
		"\u0185\u0187\7Z\2\2\u0186\u0182\3\2\2\2\u0186\u0184\3\2\2\2\u0187\u0188"+
		"\3\2\2\2\u0188\u0189\5q9\2\u0189\u018a\7\60\2\2\u018a\u018b\5q9\2\u018b"+
		"\u018c\t\b\2\2\u018c\u018d\5e\63\2\u018d\u0190\5g\64\2\u018e\u0191\t\6"+
		"\2\2\u018f\u0191\3\2\2\2\u0190\u018e\3\2\2\2\u0190\u018f\3\2\2\2\u0191"+
		"t\3\2\2\2\u0192\u0195\5m\67\2\u0193\u0195\5s:\2\u0194\u0192\3\2\2\2\u0194"+
		"\u0193\3\2\2\2\u0195v\3\2\2\2\u0196\u0197\7\f\2\2\u0197x\3\2\2\2\u0198"+
		"\u0199\n\t\2\2\u0199z\3\2\2\2\u019a\u01a2\7$\2\2\u019b\u01a1\5y=\2\u019c"+
		"\u019d\7^\2\2\u019d\u01a1\7$\2\2\u019e\u019f\7^\2\2\u019f\u01a1\7^\2\2"+
		"\u01a0\u019b\3\2\2\2\u01a0\u019c\3\2\2\2\u01a0\u019e\3\2\2\2\u01a1\u01a4"+
		"\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01a5\3\2\2\2\u01a4"+
		"\u01a2\3\2\2\2\u01a5\u01a6\7$\2\2\u01a6|\3\2\2\2\u01a7\u01b0\7$\2\2\u01a8"+
		"\u01af\5y=\2\u01a9\u01af\7\f\2\2\u01aa\u01ab\7^\2\2\u01ab\u01af\7$\2\2"+
		"\u01ac\u01ad\7^\2\2\u01ad\u01af\7^\2\2\u01ae\u01a8\3\2\2\2\u01ae\u01a9"+
		"\3\2\2\2\u01ae\u01aa\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01b2\3\2\2\2\u01b0"+
		"\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3\3\2\2\2\u01b2\u01b0\3\2"+
		"\2\2\u01b3\u01b4\7$\2\2\u01b4~\3\2\2\2\u01b5\u01b9\5[.\2\u01b6\u01b9\5"+
		"]/\2\u01b7\u01b9\t\n\2\2\u01b8\u01b5\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b8"+
		"\u01b7\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01b8\3\2\2\2\u01ba\u01bb\3\2"+
		"\2\2\u01bb\u0080\3\2\2\2\u01bc\u01bd\7%\2\2\u01bd\u01be\7k\2\2\u01be\u01bf"+
		"\7p\2\2\u01bf\u01c0\7e\2\2\u01c0\u01c1\7n\2\2\u01c1\u01c2\7w\2\2\u01c2"+
		"\u01c3\7f\2\2\u01c3\u01c4\7g\2\2\u01c4\u01c8\3\2\2\2\u01c5\u01c7\7\"\2"+
		"\2\u01c6\u01c5\3\2\2\2\u01c7\u01ca\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c8\u01c9"+
		"\3\2\2\2\u01c9\u01cb\3\2\2\2\u01ca\u01c8\3\2\2\2\u01cb\u01cc\7$\2\2\u01cc"+
		"\u01cd\5\177@\2\u01cd\u01ce\7$\2\2\u01ce\u01cf\bA\2\2\u01cf\u0082\3\2"+
		"\2\2\u01d0\u01d1\7\61\2\2\u01d1\u01d2\7\61\2\2\u01d2\u01d6\3\2\2\2\u01d3"+
		"\u01d5\n\13\2\2\u01d4\u01d3\3\2\2\2\u01d5\u01d8\3\2\2\2\u01d6\u01d4\3"+
		"\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01d9\3\2\2\2\u01d8\u01d6\3\2\2\2\u01d9"+
		"\u01da\7\f\2\2\u01da\u01db\bB\3\2\u01db\u0084\3\2\2\2\u01dc\u01dd\7\61"+
		"\2\2\u01dd\u01de\7,\2\2\u01de\u01e2\3\2\2\2\u01df\u01e1\13\2\2\2\u01e0"+
		"\u01df\3\2\2\2\u01e1\u01e4\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e2\u01e0\3\2"+
		"\2\2\u01e3\u01e5\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e5\u01e6\7,\2\2\u01e6"+
		"\u01e7\7\61\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01e9\bC\4\2\u01e9\u0086\3\2"+
		"\2\2\u01ea\u01ec\t\f\2\2\u01eb\u01ea\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed"+
		"\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01ef\3\2\2\2\u01ef\u01f0\bD"+
		"\5\2\u01f0\u0088\3\2\2\2\32\2\u014a\u014f\u0151\u015b\u015e\u0162\u0167"+
		"\u0175\u0179\u0180\u0186\u0190\u0194\u01a0\u01a2\u01ae\u01b0\u01b8\u01ba"+
		"\u01c8\u01d6\u01e2\u01ed\6\3A\2\3B\3\3C\4\3D\5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}