package fr.ensimag.deca.tree;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;


public class MethodAsmBody extends AbstractMethodBody {
    
    private static final Logger LOG = Logger.getLogger(MethodAsmBody.class);


    protected StringLiteral stringLiteral;

    public StringLiteral getStringLiteral() {
        return stringLiteral;
    }

    public MethodAsmBody(StringLiteral stringLiteral) {
        Validate.notNull(stringLiteral);
        this.stringLiteral = stringLiteral;
    }

    @Override
    public void decompile(IndentPrintStream s) {s.print(stringLiteral.getValue());}

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
            LOG.debug("[StringLiteral][verifyExpr]");
            stringLiteral.verifyExpr(compiler, localEnv, currentClass);
    }

    @Override
    public void codeGenMethodBody(DecacCompiler compiler) throws CodeGenError {
        compiler.addComment(" ---------ClassBodyAsmCodeGeneration------");
        StringBuilder buildToEliminateQuotation = new StringBuilder();
        buildToEliminateQuotation.append(stringLiteral.getValue()); 
        buildToEliminateQuotation.deleteCharAt(0);
        buildToEliminateQuotation.deleteCharAt(buildToEliminateQuotation.length()-1);
        compiler.getProgram().add(new InlinePortion(buildToEliminateQuotation.toString()));

    }

}
