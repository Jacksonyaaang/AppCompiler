package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of fields (e
 * class  A {
 *      protected int x = 5;
 * }
 * 
 * @author gl15
 * @date 01/01/2023
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    
    private static final Logger LOG = Logger.getLogger(ListDeclParam.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam v : getList()){
            v.decompile(s);
            s.println();
        }
    }

    public Signature verifyListDeclParam(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            LOG.debug("[ListDeclParam][verifyListDeclParam]");
        Signature signature = new Signature();
        for (AbstractDeclParam declParam : getList()){
            signature.add(declParam.verifyDeclParam(compiler, localEnv, currentClass));
        }
        return signature;
    }
}


