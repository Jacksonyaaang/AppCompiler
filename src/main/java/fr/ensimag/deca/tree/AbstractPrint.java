package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Print statement (print, println, ...).
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractPrint extends AbstractInst {

    private static final Logger LOG = Logger.getLogger(AbstractPrint.class);

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass, Type returnType)
        throws ContextualError {
        LOG.debug("[AbstractPrint][verifyInst] Verify the expressions in the print instruction");
        for (AbstractExpr expr : arguments.getList()){
            expr.setType(expr.verifyExpr(compiler, localEnv, currentClass));
            if (expr.getType().isBoolean())
                throw  new ContextualError("L'instruction print est incompatible avec un booléen ", getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        if (printHex == true){
            compiler.setPrintHex(true);
        }
        else{
            compiler.setPrintHex(false);
        }
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler);
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    protected abstract String getPrintName();

    protected abstract String getPrintxName();
    @Override
    public void decompile(IndentPrintStream s) {
        if(getPrintHex()){s.print(getPrintxName());}
        else{s.print(getPrintName());}
        s.print("(");
        getArguments().decompile(s);
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
