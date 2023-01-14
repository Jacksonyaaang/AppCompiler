package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
//import fr.ensimag.ima.pseudocode.instructions.WBOOL;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;

import java.io.PrintStream;
import java.util.Stack;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

    
/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);

    private GPRegister registerDeRetour = null;

    public GPRegister getRegisterDeRetour() {
        return registerDeRetour;
    }

    public void setRegisterDeRetour(GPRegister registerDeRetour) {
        this.registerDeRetour = registerDeRetour;
    }



    /**
     * Cette structure de donnée stocke la liste des registeurs dans un ordre temporel
     * qui seront retournée
     */
    private Stack<GPRegister> registerToPop = new Stack<GPRegister>();

    public Stack<GPRegister> getRegisterToPop() {
        return registerToPop;
    }

    public void emptyRegisterToPop() {
        while(registerToPop.size() != 0){
            registerToPop.pop();    
        }    
    }


    public void transferPopRegisters(Stack<GPRegister> stackToCopy){
        Stack<GPRegister> tempStack = new Stack<GPRegister>();
        while(stackToCopy.size() != 0){
            tempStack.push(stackToCopy.pop());
        }
        while(tempStack.size() != 0){
            registerToPop.push(tempStack.pop());
        }
    }


    public GPRegister getPeekRegisterToPop() {
        if ( registerToPop.size() !=0 ){
            return registerToPop.peek();
        }
        return null;

    }

    public void popRegisters(DecacCompiler compiler) {
        while (registerToPop.size() != 0){
            compiler.addInstruction(new POP(registerToPop.pop()));
        }
    }

    public void printPopRegisters(DecacCompiler compiler) {
        for (GPRegister register : registerToPop){
            LOG.debug("[AbstractExpr][printPopRegisters] PopRegister contains "+ register);
        }
    }

    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        LOG.debug("[AbstractExpr][verifyRValue] Verify the right expression of (implicit) assignments" );
        //Vérification du membre de droite d'une affectation
        Type t = this.verifyExpr(compiler, localEnv, currentClass);

        // Conversion du membre droit en float s'il est de tye int et que le membre de gauche est de type float
        if (expectedType.isFloat() && t.isInt()){
            ConvFloat cF = new ConvFloat(this);
            cF.verifyExpr(compiler, localEnv, currentClass);
            LOG.debug("[Assign][verifyExpr] Conv int -> float");
            return cF;
        }
        if (!expectedType.sameType(t))
            throw new ContextualError("Not expected type", getLocation());
        setType(expectedType);
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("[AbstractExpr][verifyInst] Verify the expression coming from the instruction");
        verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("[AbstractExpr][verifyInst] Verify the condition of a While or ifEhenElse");
        Type type_cond = verifyExpr(compiler, localEnv, currentClass);
        //Si le type de la condition est null ou n'est pas boolean, on jette une ContextualError
        if (type_cond != null && type_cond.isBoolean()) setType(type_cond);
        else{
            throw new ContextualError("la condition doit être booléan", getLocation());
        }
        setType(compiler.environmentType.BOOLEAN);
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) throws CodeGenError {
        this.codeGenInst(compiler);
        /* this
        */
        this.emptyRegisterToPop();
        LOG.debug("[AbstractExpr][codeGenPrint]Method has been visited wity type " + this.getType());
        if(getType() == compiler.environmentType.INT){
            LOG.debug("[AbstractExpr][codeGenPrint] Priting an int");
            compiler.addInstruction(new LOAD(this.registerDeRetour, Register.getR(1)));
            compiler.addInstruction(new WINT());
        }
        else if(getType() == compiler.environmentType.FLOAT){
            LOG.debug("[AbstractExpr][codeGenPrint] Priting an flaot");
            compiler.addInstruction(new LOAD(this.registerDeRetour, Register.getR(1)));
            if(!(compiler.isPrintHex()))
                compiler.addInstruction(new WFLOAT());
            else{
                compiler.addInstruction(new WFLOATX());
            }
        }
        else if(getType() == compiler.environmentType.BOOLEAN){
            //A faire: eliminated this part of the code since it we can not reach it 
            //due to contextual errors
            LOG.debug("[AbstractExpr][codeGenPrint] Priting an ");
            compiler.addInstruction(new LOAD(this.registerDeRetour, Register.getR(1)));
            compiler.addInstruction(new WINT());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        LOG.debug("[Abstractexpr][codeGenInst] I have visited abstract expr");
        throw new CodeGenError(getLocation(), "[Abstractexpr][codeGenInst]Cette méthode ne doit jamais être appélée");
    }

    /**
     * Cette méthodes est utilisé par les literaux et les indentificateur à fin de mettre leur valeur 
     * dans un registre, la méthode loadItemintoRegister est custom à chaque type par exemple pour les indentificateur 
     * ca cherche les adresses et pour le literaux ca prend leur valeur brute et la positionne dans un registre 
     */
    protected GPRegister LoadGencode(DecacCompiler compiler, boolean loadItemintoRegister) throws CodeGenError {
        GPRegister regReserved = null;
        if (compiler.getRegisterManagement().areThereAnAvaliableRegsiterSup2()){
            regReserved = compiler.getRegisterManagement().getAnEmptyStableRegisterAndReserveIt(); 
            assert(regReserved !=null );
            LOG.debug("[Abstractexpr][LoadGencode]  Reserving an non empty register with the name " + regReserved);
        }
        else{
            regReserved = compiler.getRegisterManagement().getAUsedStableRegisterAndKeepItReserved(); 
            assert(regReserved !=null );
            LOG.debug("[Abstractexpr][LoadGencode]  Reserving an used register with the name " + regReserved);
            compiler.addInstruction(new PUSH(regReserved));
            this.getRegisterToPop().push(regReserved);
        }
        if (loadItemintoRegister){
            this.loadItemintoRegister(compiler, regReserved);
        }
        return regReserved;
    }
    
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister regReserved) throws CodeGenError {
        throw new CodeGenError(getLocation(), "[AbstractExpr] This method should not be called at this level, loadItemintoRegister");
    }

    

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    public Boolean checkIfExprIsTerminal(AbstractExpr expr){
        if (expr instanceof Identifier){
            return true;
        }
        else if (expr instanceof BooleanLiteral ){
            return true;
        }
        else if (expr instanceof FloatLiteral){
            return true;
        }
        else if (expr instanceof IntLiteral){
            return true;
        }
        return false;

    }
    
}
