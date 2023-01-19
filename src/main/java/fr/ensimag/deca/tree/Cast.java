    package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.syntax.DecaParser.ExprContext;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

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

    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        // GPRegister R;
        // if(this.typeCast.getName()==this.expr.getType().getName()){
        //     this.expr.LoadGencode(compiler,false);

        // }
        // else if((this.typeCast.getDefinition().getType().isFloat())&&(this.expr.getType().isInt())){
        //     R=this.expr.LoadGencode(compiler,true);
        //     compiler.addInstruction(new FLOAT(R, R));
        // }
        // else if((this.typeCast.getDefinition().getType().isInt())&&(this.expr.getType().isFloat())){
        //     R=this.expr.LoadGencode(compiler,true);
        //     compiler.addInstruction(new INT(R, R));
        // }
        // else if((this.typeCast.getDefinition().getType().isClass())&&(this.expr.getType().isClass())){
        //     if((this.expr instanceof this.typeCast)||(this.expr==null)){
        //         //pas sûr pour la comparaison avec null
        //         this.expr.LoadGencode(compiler,false);
        //     }
        //     else{
        //         throw new CodeGenError(getLocation(),"Expression "+ this.expr+" is not of class "+this.typeCast);
        //     }
        // }
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // TODO Auto-generated method stub
        return null;
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
    }
}
