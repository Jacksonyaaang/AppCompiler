package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;


public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
 
    private static final Logger LOG = Logger.getLogger(ListDeclMethod.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod m : getList()){
            m.decompile(s);
            s.println();
        }

    }

    void verifyListDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            LOG.debug("[ListDeclMethod][verifyListDeclMethod]");
        for (AbstractDeclMethod declMethod : getList()){
            LOG.info("[ListDeclMethod][verifyListDeclMethod] Verifing the method : "+((DeclMethod)declMethod).getMethodName().getName().getName());
            declMethod.verifyDeclMethodSimple(compiler, localEnv, currentClass);
        }
    }

    void verifyListDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            LOG.debug("[ListDeclMethod][verifyListDeclMethod]");
        for (AbstractDeclMethod declMethod : getList()){
            declMethod.verifyDeclMethod(compiler, localEnv, currentClass);
        }
    }

}
