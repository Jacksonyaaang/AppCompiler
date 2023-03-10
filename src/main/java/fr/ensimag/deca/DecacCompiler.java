package fr.ensimag.deca;

import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.codegen.RegisterManagementUnit;
import fr.ensimag.deca.codegen.StackManagementUnit;
import fr.ensimag.deca.codegen.TableDeMethode;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.codegen.ListError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.Symbol
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl15
 * @date 01/01/2023
 */
public class DecacCompiler {

    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    private int opBoolIncrementer = 0;
    private int ifIncrementer = 0;
    private int whileIncrementer = 0;
    private int instanceOfIncrementer = 0;
    private int castIncrement = 0;

    public int getInstanceOfIncrementer() {
        return instanceOfIncrementer;
    }
    public int incrementInstanceOfIncrementer() {
        instanceOfIncrementer++;
        return instanceOfIncrementer;
    }

    public int getCastIncrement() {
        return castIncrement;
    }
    public int incrementCastIncrement() {
        castIncrement++;
        return castIncrement;
    }

    public int getOpBoolIncrementer() {
        return opBoolIncrementer;
    }
    public int incrementOpBoolIncrementer() {
        opBoolIncrementer++;
        return opBoolIncrementer;
    }

    public int getIfIncrementer() {
        return ifIncrementer;
    }
    public int incrementIfIncrementer() {
        ifIncrementer++;
        return ifIncrementer;
    }
    
    public int getWhileIncrementer() {
        return whileIncrementer;
    }
    public int incrementWhileIncrementer() {
        whileIncrementer++;
        return whileIncrementer;
    }

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
    }

    public void setMainProgramState(){
        if (mainManagementUnitsSaved){
            LOG.debug("[DecacCompiler][setMainProgramState] Seeting main state program");
            registerManagement = registerManagementPlaceHolder;
            stackManagement = stackManagementPlaceHolder;
            program = programPlaceHolder;
            InMainOrMethod = true;
        }
        else{
            LOG.fatal("[DecacCompiler][setMainProgramState] Trying to return to mainManagementWhile they are not saved !!!");
        }
    }

    public void saveMainProgramState(){
        if (InMainOrMethod){
            LOG.debug("[DecacCompiler][saveMainProgramState] Saving state main program");
            registerManagementPlaceHolder = registerManagement;
            stackManagementPlaceHolder = stackManagement;
            programPlaceHolder = program;
            mainManagementUnitsSaved = true;
        }
        else{
            LOG.fatal("[DecacCompiler][saveMainProgramState] Tying to save main program state while in a method !!!");
        }
    }

    public void setMethodProgramState(RegisterManagementUnit methodManagementUnit, StackManagementUnit methodStackManagementUnit, IMAProgram methodProgram){
        if (mainManagementUnitsSaved){
            LOG.debug("[DecacCompiler][setMethodProgramState] Entring a method program");
            registerManagement = methodManagementUnit;
            stackManagement = methodStackManagementUnit;
            program = methodProgram;
            InMainOrMethod = false;
        }
        else{
            LOG.fatal("[DecacCompiler][setMethodProgramState] Trying to switch to a method while the main program is not saved !!!");
        }
    }

    /**
     * InMainOrMethod indique si le compilateur est en train d'??crire dans une methode ou dans un main
     * True: main; False:method
     */
    protected boolean InMainOrMethod = true;
    
    /**
     * mainManagementUnitsSaved indique si les unit de management de register/stack/program
     * sont sauv??e ou non
     */
    protected boolean mainManagementUnitsSaved = false;

    /**
     * currentMethodCodeGen stocke la definition de la m??thode au quel on manipule.
     * cette valeur sera utilis??e pour generer le retour de la fonction
     */
    protected MethodDefinition currentMethodCodeGen = null;


    public MethodDefinition getCurrentMethodCodeGen() {
        return currentMethodCodeGen;
    }
    public void setCurrentMethodCodeGen(MethodDefinition currentMethodCodeGen) {
        this.currentMethodCodeGen = currentMethodCodeGen;
    }

    /**
     * Unit?? qui fait la gestion des registres utils??es dans la partie 
     */
    private RegisterManagementUnit registerManagement  ;
    private RegisterManagementUnit registerManagementPlaceHolder  ;

    public void setRegisterManagement(RegisterManagementUnit registerManagement) {
        this.registerManagement = registerManagement;
    }

    public RegisterManagementUnit getRegisterManagement() {
        return registerManagement;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;


    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();
    private IMAProgram programPlaceHolder = program;

    public void setProgram(IMAProgram program) {
        this.program = program;
    }

    public IMAProgram getProgram() {
        return program;
    }

    /**
     * Cette unit?? est utilis??e pour associer des adresses ?? des variables,
     * et elle est aussi utilis??e pour calculer le nombre de variable temporaire necessaire pour executer 
     * les blocs 
     */
    public StackManagementUnit stackManagement = new StackManagementUnit();  
    public StackManagementUnit stackManagementPlaceHolder = stackManagement;  

    public void setStackManagement(StackManagementUnit stackManagement) {
        this.stackManagement = stackManagement;
    }

    public StackManagementUnit getStackManagement() {
        return stackManagement;
    }

    public int incrementGbCompiler() {
        return stackManagement.incrementGbCounter();
    }

    public int incrementLbCompiler() {
        return stackManagement.incrementLbCounter();
    }

    public int decrementParamCounterCompiler() {
        return stackManagement.decrementParamCounter();
    }

    /**
     * Cette unit?? est utilis??e pour associer des adresses ?? des variables,
     * et elle est aussi utilis??e pour calculer le nombre de variable temporaire necessaire pour executer 
     * les blocs 
     */
    public final ListError errorManagementUnit = new ListError();  

    public ListError getErrorManagementUnit() {
        return errorManagementUnit;
    }

    /**
     * Cette unit?? est utilis??e pour associer ?? des table de m??thode des adresses.
     */
    public final TableDeMethode tableDeMethodeCompiler = new TableDeMethode();  

    public TableDeMethode getTableDeMethodeCompiler() {
        return tableDeMethodeCompiler;
    }

    /** The global environment for types (and the symbolTable) */
    public final SymbolTable symbolTable = new SymbolTable();
    public final EnvironmentType environmentType = new EnvironmentType(this);


    public Symbol createSymbol(String name) {
        return symbolTable.create(name);
    }

    private boolean printHex;

    public boolean isPrintHex() {
        return printHex;
    }

    public void setPrintHex(boolean printHex) {
        this.printHex = printHex;
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        StringBuilder destination = new StringBuilder();
        destination.append(sourceFile);
        destination.replace(sourceFile.length()-4, sourceFile.length(), "ass");
        String destFile = destination.toString() ;
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            registerManagement = new RegisterManagementUnit(compilerOptions.getNumberOfRegisters());
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException, CodeGenError {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());
        if (compilerOptions.isDecompile()) {
            prog.decompile(out);
            return false;
        }   

        prog.verifyProgram(this);
        assert(prog.checkAllDecorations());

        if (compilerOptions.isVerfiryAndStop()){
            return false;
        }

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

}
