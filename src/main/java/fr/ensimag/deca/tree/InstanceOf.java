package fr.ensimag.deca.tree;

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
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;


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

    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        this.expr.codeGenInst(compiler);
        GPRegister reg = expr.getRegisterDeRetour();
        GPRegister RType = this.LoadGencode(compiler, false);
        Label loopbegin = new Label("loopbegin");
        Label endtrue = new Label("endtrue");
        Label endfalse = new Label("endfalse");
        compiler.addInstruction(new LOAD(compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(typeInstance.getClassDefinition()), RType), 
                                "loading method table of " + typeInstance.getName());
        // ^=on récupère la position de la classe de typeInstance dans RType
        compiler.addLabel(loopbegin);  //
        compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg));
        compiler.addInstruction(new CMP(new NullOperand(), reg));
        compiler.addInstruction(new BEQ(endfalse));
        compiler.addInstruction(new CMP(reg,RType));
        compiler.addInstruction(new BNE(loopbegin));
        compiler.addInstruction(new LOAD(1, RType));
        compiler.addInstruction(new BRA(endtrue));
        compiler.addLabel(endfalse);
        compiler.addInstruction(new LOAD(0, RType));
        compiler.addLabel(endtrue);
        expr.popRegisters(compiler);
        compiler.getRegisterManagement().decrementOccupationRegister(expr.getRegisterDeRetour());
        this.setRegisterDeRetour(RType);
    
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
        s.print("(");
        getExpr().decompile(s);
        s.print(" instanceof ");
        getTypeInstance().decompile(s);
        s.print(")");
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
