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

    private GPRegister registerDeRetour;

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

    public void popRegisters(DecacCompiler compiler) {
        while (registerToPop.size() != 0){
            compiler.addInstruction(new POP(registerToPop.pop()));;
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
        System.out.println("On est dans AbstractExpr.java");
        try{
            Type t = verifyExpr(compiler, localEnv, currentClass);
            //if(!(expectedType.isFloat() && t.isInt()) || !expectedType.sameType(t)){
            if (!expectedType.sameType(t))
                throw new ContextualError("Not exepected type", getLocation());
            //}
        } catch (ContextualError e){
            e.fillInStackTrace();
        }
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        System.out.println("On est dans AbstractExpr.java");
            verifyExpr(compiler, localEnv, currentClass);
        //throw new UnsupportedOperationException("not yet implemented");
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
            Type type_cond = verifyExpr(compiler, localEnv, currentClass);
            if (type_cond != null && type_cond.isBoolean()) setType(type_cond);
            else{
                throw new ContextualError("la condition doit être booléan", getLocation());
            }
        //throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) throws CodeGenError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        //A FAIRE
        LOG.debug("i have visited abstract expr");
        System.out.println("i have visited abstract expr");
    }

    protected GPRegister LoadGencode(DecacCompiler compiler) throws CodeGenError {
        GPRegister regReserved = null;
        if (compiler.getRegisterManagement().isThereAnAvaliableRegsiterSup2()){
            regReserved = compiler.getRegisterManagement().getAnEmptyStableRegisterAndReserveIt(); 
            compiler.pushGlobalRegisterStack(regReserved);
        }
        else{
            regReserved = compiler.getRegisterManagement().getAUsedStableRegisterAndKeepItReserved(); 
            compiler.addInstruction(new PUSH(regReserved));
            this.getRegisterToPop().push(regReserved);
        }
        this.loadItemintoRegister(compiler, regReserved);
        return regReserved;
    }
    
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister regReserved) throws CodeGenError {
        throw new CodeGenError("This method should not be called at this level, loadItemintoRegister");
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
