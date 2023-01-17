package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.syntax.DecaParser.ExprContext;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Cast extends AbstractExpr{

    protected AbstractIdentifier typeCast;
    protected AbstractExpr expr;

    public AbstractIdentifier getTypeCast() {
        return typeCast;
    }
    public AbstractExpr getExpr() {
        return expr;
    }
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        
    }

    public Cast(AbstractIdentifier typeCast, AbstractExpr expr){
        Validate.notNull(typeCast);
        Validate.notNull(expr);
        this.typeCast = typeCast;
        this.expr = expr;
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
        typeCast.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true); 
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        typeCast.iter(f);
        expr.iter(f);         
    }
    
}
