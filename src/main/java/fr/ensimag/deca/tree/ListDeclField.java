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



}

