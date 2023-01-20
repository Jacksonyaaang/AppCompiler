package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class BooleanLiteral extends AbstractExpr {

    private static final Logger LOG = Logger.getLogger(BooleanLiteral.class);
    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{   
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        compiler.addInstruction(new LOAD(new ImmediateInteger(value ? 1 :0) , reg),
                                     "loading "+getValue()+ " into memory");
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            LOG.debug("[BooleanLiteral][verifyExpr]");
            setType(compiler.environmentType.BOOLEAN);
            return getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

}
