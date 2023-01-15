package fr.ensimag.deca.tree;

import static org.mockito.ArgumentMatchers.booleanThat;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class This extends AbstractExpr{

    protected boolean bool ; 

    public This(boolean bool){
        this.bool = bool;
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
