package fr.ensimag.deca.tree;

import static org.mockito.ArgumentMatchers.booleanThat;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

public class This extends AbstractExpr{

    private static final Logger LOG = Logger.getLogger(This.class);
    protected boolean bool ; 

    public This(boolean bool){
        this.bool = bool;
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
