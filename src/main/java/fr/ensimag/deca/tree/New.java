package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class New  extends AbstractExpr{

    protected AbstractIdentifier className;



    public New(AbstractIdentifier className){
        Validate.notNull(className);
        this.className = className;
    }

    public AbstractIdentifier getClassName() { return className;}

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
            Type t = className.verifyType(compiler);
            if(!t.isClass()){
                throw new ContextualError("L'expression new est utilisée uniquement pour créer des objets dont le type est un type de classe", getLocation());
            }
            setType(t);
        return t;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, true);
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);                 
    }
    
}
