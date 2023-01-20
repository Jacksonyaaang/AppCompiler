package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclParam extends Tree {


    /**
     * verify the declaration of parameters of a method 
     * @param compiler for environment types 
     * @return
     * @throws ContextualError
     */
    protected abstract Type verifyDeclParam(DecacCompiler compiler, EnvironmentExp envParms)
            throws ContextualError;
            
    public void codeGenDecl(DecacCompiler compiler) throws CodeGenError {
        throw new CodeGenError(getLocation(), "This method must not be called at this level");
    }
    
}
