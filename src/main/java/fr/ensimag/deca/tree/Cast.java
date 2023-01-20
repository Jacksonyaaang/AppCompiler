package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
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

    public Cast(AbstractIdentifier typeCast, AbstractExpr expr){
        Validate.notNull(typeCast);
        Validate.notNull(expr);
        this.typeCast = typeCast;
        this.expr = expr;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        Type t1 = typeCast.verifyType(compiler);
        typeCast.setType(t1);
        Type t2 = expr.verifyExpr(compiler, localEnv, currentClass);
        expr.setType(t2);
        if (t2.isVoid()){
            throw new ContextualError("on peut pas caster un void", getLocation());
        }

        else if ((t1.isFloat() && !t2.isInt() && !t2.isFloat()) || (t1.isInt() && !t2.isFloat() && !t2.isInt()) ||
                (t2.isFloat() && !t1.isInt() && !t1.isFloat()) || (t2.isInt() && !t1.isFloat() && !t1.isInt())){
                    throw new ContextualError("on peut caster un Int que par un Float et vis-versa", getLocation());
        }
        else if ((t1.isClass() && t2.isClass() && !((ClassType)t1).isSubClassOf((ClassType)t2) &&
                 !((ClassType)t2).isSubClassOf((ClassType)t1)) || t2.isNull() ||
                 (t1.isNull() && !t2.isClass())){
                throw new ContextualError("cast impossible", getLocation());
        }
        //return compiler.environmentType.FLOAT;
        setType(typeCast.getType());
        return typeCast.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getTypeCast().decompile(s);
        s.print(") (");
        getExpr().decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        typeCast.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true); 
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        typeCast.iter(f) ;
        expr.iter(f);         
    }
    
}
