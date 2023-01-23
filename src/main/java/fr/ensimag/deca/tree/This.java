package fr.ensimag.deca.tree;

import static org.mockito.ArgumentMatchers.booleanThat;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

public class This extends AbstractExpr{

    protected boolean bool ;
    private static final Logger LOG = Logger.getLogger(This.class);

    @Override
    boolean isImplicit() {
        return bool;
    }


    public This(boolean bool){
        this.bool = bool;
    } 

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{   
        LOG.debug("[This][codeGenInst] loading this into memory ");
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }


    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),reg)); 
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("[This][verifyExpr]");
        if(currentClass == null){
            throw new ContextualError("L'expression this. ne peut pas appeler un objet de type Object", getLocation());
        }
        setType(currentClass.getType());
        return currentClass.getType();
    }

    

    @Override
    public void decompile(IndentPrintStream s) {
        if(isBool()){
            return;
        }
        s.print("this");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // NO chiledren
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // NO chiledren
        
    }
    
    public boolean isBool() {
        return bool;
    }


}

