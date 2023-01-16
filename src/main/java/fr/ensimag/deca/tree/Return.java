package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Return extends AbstractInst {

    protected AbstractExpr exprReturn;

    public Return(AbstractExpr exprReturn){
        Validate.notNull(exprReturn);
        this.exprReturn = exprReturn;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        getExprReturn().decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        exprReturn.prettyPrint(s, prefix, true);    
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        exprReturn.iter(f);           
    }

    public AbstractExpr getExprReturn() {
        return exprReturn;
}
}
