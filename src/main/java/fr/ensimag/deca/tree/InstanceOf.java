package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;


public class InstanceOf extends AbstractExpr {


    protected AbstractExpr expr;
    protected AbstractIdentifier typeInstance;

    public AbstractExpr getExpr() {
        return expr;
    }

    public AbstractIdentifier getTypeInstance() {
        return typeInstance;
    }

    public InstanceOf(AbstractExpr expr, AbstractIdentifier typeInstance){
        Validate.notNull(expr);
        Validate.notNull(typeInstance);
        this.expr = expr;
        this.typeInstance = typeInstance;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t1 = expr.verifyExpr(compiler, localEnv, currentClass);
        expr.setType(t1);
        if (!t1.isClassOrNull()){
            throw new ContextualError("instanceof doit être avec des classes", getLocation());
        }
        Type t2 = typeInstance.verifyType(compiler);
        typeInstance.setType(t2);
        if (!t2.isClass() || t2.isNull()){
            throw new ContextualError("l' identificateur à droite de instanceof doit être une classe non null", getLocation());
        }
        return compiler.environmentType.BOOLEAN;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);       
        typeInstance.prettyPrint(s, prefix, true);    
    }      

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);           
        typeInstance.iter(f) ;
    }
    
}
