package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import java.io.PrintStream;
import org.apache.log4j.Logger;


/**
 * Integer literal
 *
 * @author gl15
 * @date 01/01/2023
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{   
        LOG.debug("[IntLiteral][codeGenInst] generating code for int literal value " + getValue());
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        LOG.debug("[IntLiteral][loadItemintoRegister] loading "+getValue()+ " into memory at register " + reg);
        compiler.addInstruction(new LOAD(new ImmediateInteger(value) , reg),
                                     "loading "+getValue()+ " into memory"); 
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) throws ContextualError {
        LOG.debug("[IntLiteral][verifyExpr]");
        setType(compiler.environmentType.INT);
        return getType();
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
