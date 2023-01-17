package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

public class Null extends AbstractExpr{

    private static final Logger LOG = Logger.getLogger(Null.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("[Null][verifyExpr]");
//        Type t = new NullType(compiler.environmentType.de);
//        setType(t);
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // No children        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // No children
        
    }

    @Override
    String prettyPrintNode() {
        return "Null";
    }
    
}

