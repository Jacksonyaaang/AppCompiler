package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class Main extends AbstractMain {
    
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        EnvironmentExp EnvExpInit = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, EnvExpInit, null);
        insts.verifyListInst(compiler, EnvExpInit, null, null);
        LOG.debug("verify Main: end");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) throws CodeGenError{
        compiler.addComment("Beginning of main instructions:");
        declVariables.codeGenListDecl(compiler);
        insts.codeGenListInst(compiler);
        int sizeStack = compiler.getStackManagement().measureStacksizeNeededMain(compiler);
        //On reserve de l'espace dans le stack
        if (sizeStack != 0){
            compiler.getErrorManagementUnit().activeError("stack_overflow_error");
            //On reserve uniquement de l'espace dont on stock des valeurs de registres
            if (compiler.getStackManagement().getGbCounter() != 0){
                compiler.getProgram().addFirst(new ADDSP(new ImmediateInteger(compiler.getStackManagement().getGbCounter())));
            }
            if (!(compiler.getCompilerOptions().isNoCheck())) {
                compiler.getProgram().addFirst(new BOV(new Label("stack_overflow_error")));
                compiler.getProgram().addFirst(new TSTO(new ImmediateInteger(sizeStack)));
            }
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
