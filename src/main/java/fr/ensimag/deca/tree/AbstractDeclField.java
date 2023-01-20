package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Variable declaration
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractDeclField extends Tree {

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
    protected abstract void verifyDeclField(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
    
    protected abstract void verifyinitFieldPass3(DecacCompiler compiler, EnvironmentExp localEnv, 
        ClassDefinition currentClass) throws ContextualError;

    public void codeGenDelField(DecacCompiler compiler) throws CodeGenError {
        throw new CodeGenError(getLocation(), "[AbstractDeclField][codeGenField]This method must not be called at this level");
    }   

    public void CodeGenPlaceZeroInField(DecacCompiler compiler) throws CodeGenError {
        throw new CodeGenError(getLocation(), "[AbstractDeclField][CodeGenPlaceZeroInField]This method must not be called at this level");
    }

            
}

