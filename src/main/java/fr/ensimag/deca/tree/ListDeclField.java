package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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
public class ListDeclField extends TreeList<AbstractDeclField> {
    
    private static final Logger LOG = Logger.getLogger(ListDeclField.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()){
            f.decompile(s);
            s.println();
        }

    }

    // public void codeGenListDecl(DecacCompiler compiler) throws CodeGenError {
    //     for (AbstractDeclField i : getList()) {
    //         compiler.getRegisterManagement().freeAllRegisters();
    //         i.codeGenDecl(compiler);
    //     }
    // }

        /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclField(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            LOG.debug("[ListDeclField][verifyListDeclField]");
        for (AbstractDeclField declField : getList()){
            declField.verifyDeclField(compiler, localEnv, currentClass);
        }
    }

    void verifyInitFields(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            LOG.debug("[ListDeclField][verifyInitListDeclField]");
        for (AbstractDeclField declField : getList()){
            declField.verifyinitFieldPass3(compiler, localEnv, currentClass);
        }
    }

    public void CodeGenListPlaceZeroInField(DecacCompiler compiler) throws CodeGenError {
        LOG.debug("[ListDeclField][CodeGenListPlaceZeroInField] Placing Zero In All fields");
        for (AbstractDeclField declField : getList()){
            compiler.getRegisterManagement().freeAllRegisters();
            declField.CodeGenPlaceZeroInField(compiler);
        }
    }

    public void CodeGenListInitializeField(DecacCompiler compiler) throws CodeGenError {
        LOG.debug("[ListDeclField][CodeGenListInitializeField] Initializing the fields");
        for (AbstractDeclField declField : getList()){
            compiler.getRegisterManagement().freeAllRegisters();
            declField.codeGenDelField(compiler);
        }
    }

}







