package fr.ensimag.deca.tree;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;


public class MethodAsmBody extends AbstractMethodBody {
    
    protected StringLiteral stringLiteral;

    public StringLiteral getStringLiteral() {
        return stringLiteral;
    }

    public MethodAsmBody(StringLiteral stringLiteral) {
        Validate.notNull(stringLiteral);
        this.stringLiteral = stringLiteral;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        stringLiteral.prettyPrint(s, prefix, true);        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        stringLiteral.iter(f);   
    }

    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type retunType) throws ContextualError {
        // TODO Auto-generated method stub
        
    }
}
