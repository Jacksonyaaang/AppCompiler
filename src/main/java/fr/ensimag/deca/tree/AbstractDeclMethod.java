package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.IMAProgram;

public abstract class AbstractDeclMethod extends Tree {

    AbstractIdentifier type;
    AbstractIdentifier name;
    ListDeclParam params;
    MethodBody body;

    public AbstractIdentifier getType() {
        return type;
    }

    public AbstractIdentifier getName() {
        return name;
    }

    public ListDeclParam getParams() {
        return params;
    }

    public MethodBody getBody() {
        return body;
    }

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

     /**une version simple pour la passe deux  */
    protected abstract void verifyDeclMethodSimple(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /** une version normal pour la troisième passe*/
    protected abstract void verifyDeclMethod(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
            
    public void codeGenDeclMethod(DecacCompiler compiler) throws CodeGenError {
        throw new CodeGenError(getLocation(), "This method must not be called at this level");
    }
    

    /**
     * Retourne le program associé à la méthode
     * @return
     */
    public IMAProgram getMethodProgram() {
        return null;
    }


}
