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
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError {
        GPRegister R=compiler.getRegisterManagement().getAnEmptyStableRegisterAndReserveIt();
        this.setRegisterDeRetour(R);
        compiler.addInstruction(new LOAD(-2(LB),R)); 
        compiler.addInstruction(new CMP(null, R)); 
        compiler.addInstruction(new BEQ(new Label("deref_null_error"))); 
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("[This][verifyExpr]");
        if(currentClass.getType().sameType(compiler.environmentType.OBJECT)){
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

