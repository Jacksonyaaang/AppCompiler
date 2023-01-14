package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;

import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class ReadFloat extends AbstractReadExpr {

    private static final Logger LOG = Logger.getLogger(And.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) throws ContextualError {
        LOG.debug("[ReadFloat][verifyExpr]");
        setType(compiler.environmentType.FLOAT);
        return getType();  
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError
     {
        LOG.debug("[ReadFloat][CodeGenInst] generating code for ReadFloat");
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
        LOG.debug("[ReadFloat][codeGenInst] exiting generation method method");
    }

    
    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        LOG.debug("[ReadFloat][codeGenInst] Convering value stored in the reg" + reg + "register");
        compiler.addInstruction(new RFLOAT());
        compiler.addInstruction(new BOV(new Label("io_error")));
        compiler.getErrorManagementUnit().activeError("io_error");
        compiler.addInstruction(new LOAD(Register.getR(1), reg));
    }
    


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
