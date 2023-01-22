package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.syntax.DecaParser.ExprContext;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
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
        compiler.incrementCastIncrement();
        if( ((Identifier) typeCast).getType() == this.expr.getType()){
            expr.codeGenInst(compiler);
        }
        else if((( (Identifier) typeCast).getType().isFloat())&&(expr.getType().isInt())){
            expr.codeGenInst(compiler);
            compiler.addInstruction(new FLOAT(expr.getRegisterDeRetour(), expr.getRegisterDeRetour()));
        }
        else if((this.typeCast.getDefinition().getType().isInt())&&(expr.getType().isFloat())){
            expr.codeGenInst(compiler);
            compiler.addInstruction(new INT(expr.getRegisterDeRetour(), expr.getRegisterDeRetour()));
        }
        else if (expr instanceof Null){
            expr.codeGenInst(compiler);
        }
        else if((this.typeCast.getType().isClass())&&(this.expr.getType().isClass())){
            InstanceOf verificationClassInstance = new InstanceOf(expr, typeCast);
            verificationClassInstance.codeGenInst(compiler);
            compiler.addInstruction(new CMP(new ImmediateInteger(1), verificationClassInstance.getRegisterDeRetour()));
            compiler.addInstruction(new BEQ(new Label("load_item_and_leave_cast"+compiler.getCastIncrement())));
            if (!(compiler.getCompilerOptions().isNoCheck())){
                compiler.getErrorManagementUnit().activeError("cast_error");
                compiler.addInstruction(new BRA(new Label("cast_error")));
            }
            compiler.addLabel(new Label("load_item_and_leave_cast"+compiler.getCastIncrement()));
            verificationClassInstance.popRegisters(compiler);
            compiler.getRegisterManagement().decrementOccupationRegister(expr.getRegisterDeRetour());
            expr.codeGenInst(compiler);
        }
        this.setRegisterDeRetour(expr.getRegisterDeRetour());
        this.transferPopRegisters(expr.getRegisterToPop());
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        Type t1 = typeCast.verifyType(compiler);
        typeCast.setType(t1);
        Type t2 = expr.verifyExpr(compiler, localEnv, currentClass);
        expr.setType(t2);
        if (t2.isVoid()){
            throw new ContextualError("On ne peut pas convertir le type d'une expression de type void", getLocation());
        }

        else if ((t1.isFloat() && !t2.isInt() && !t2.isFloat()) || (t1.isInt() && !t2.isFloat() && !t2.isInt()) ||
                (t2.isFloat() && !t1.isInt() && !t1.isFloat()) || (t2.isInt() && !t1.isFloat() && !t1.isInt())){
                    throw new ContextualError("On ne peut que convertir un Int en Float et vice-versa", getLocation());
        }
        else if ((t1.isClass() && t2.isClass() && !((ClassType)t1).isSubClassOf((ClassType)t2) &&
                 !((ClassType)t2).isSubClassOf((ClassType)t1)) || t2.isNull() ||
                 (t1.isNull() && !t2.isClass())){
                throw new ContextualError("Cast impossible entre ces deux types", getLocation());
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
