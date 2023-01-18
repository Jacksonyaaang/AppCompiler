package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("[ListDeclClass][verifyListClass] verify listClass pass 1: START");
        for (AbstractDeclClass DeclClass : getList()) {
            DeclClass.verifyClass(compiler);    
        }
        LOG.debug("[ListDeclClass][verifyListClass] verify listClass pass 1: END");

    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("[ListDeclClass][verifyListClassMembers] verify listClass pass 2: START");
        for (AbstractDeclClass DeclClass : getList()) {
            DeclClass.verifyClassMembers(compiler);
        }
        LOG.debug("[ListDeclClass][verifyListClassMembers] verify listClass pass 2: END");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("[ListDeclClass][verifyListClassBody] verify listClass pass 3: START");
        for (AbstractDeclClass DeclClass : getList()) {
            DeclClass.verifyClassBody(compiler);
        }
        LOG.debug("[ListDeclClass][verifyListClassBody] verify listClass pass 3: END");
    }

    public void  codeGenListClassTableau(DecacCompiler compiler) throws CodeGenError{
        LOG.debug("[ListDeclClass][codeGenListClassTableau] Code generation pass 1: START");
        for (AbstractDeclClass DeclClass : getList()) {
            DeclClass.codeGenTableauDeMethod(compiler); 
        }
        LOG.debug("[ListDeclClass][codeGenListClassTableau] Code generation pass 1: END");
    }


}
