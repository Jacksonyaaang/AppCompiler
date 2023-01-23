package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import org.apache.log4j.Logger;

public class Null extends AbstractExpr{

    private static final Logger LOG = Logger.getLogger(Null.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        LOG.debug("[Null][verifyExpr]");
        setType(compiler.environmentType.NULL);
        return getType();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{   
        LOG.debug("[Null][codeGenInst] generating code for null expr " );
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        LOG.debug("[Null][loadItemintoRegister] loading 0 into the register " + reg);
        compiler.addInstruction(new LOAD(new NullOperand() , reg),
                                     "loading 0 into the register for null expr"); 
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // No children        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // No children
        
    }

    @Override
    String prettyPrintNode() {
        return "Null";
    }
    
}

