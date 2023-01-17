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

public class This extends AbstractExpr{

    protected boolean bool ; 

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        
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
