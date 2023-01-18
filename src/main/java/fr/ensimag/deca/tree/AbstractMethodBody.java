package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractMethodBody extends Tree{

    /**
     * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */    

    /** une version normal pour la troisième passe*/
    protected abstract void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, 
                                                ClassDefinition currentClass, Type retunType)
                                                throws ContextualError;
        
    public void codeGenDecl(DecacCompiler compiler) throws CodeGenError {
        throw new CodeGenError(getLocation(), "This method must not be called at this level");
    }

    
}
