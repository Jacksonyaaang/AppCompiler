package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import net.bytebuddy.jar.asm.Label;

import org.apache.commons.lang.Validate;
import fr.ensimag.deca.context.*;


public class MethodBody extends AbstractMethodBody {
    
    protected  ListDeclVar listVar;

    protected  ListInst listInstructions;

    public ListDeclVar getListVar() {
        return listVar;
    }

    public ListInst getListInstructions() {
        return listInstructions;
    }

    public MethodBody(ListDeclVar listVar, ListInst listInstructions) {
        Validate.notNull(listVar);
        Validate.notNull(listInstructions);
        this.listVar = listVar;
        this.listInstructions = listInstructions;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        getListVar().decompile(s);
        getListInstructions().decompile(s);
        s.unindent();
        s.print("}");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) { 
        listVar.prettyPrint(s, prefix, false);
        listInstructions.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        listVar.iter(f);
        listInstructions.iter(f);
    }

    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type retunType)
        throws ContextualError {
        listVar.verifyListDeclVariable(compiler, localEnv, currentClass);
        listInstructions.verifyListInst(compiler, localEnv, currentClass, retunType);    
    }

    @Override
    public void codeGenMethodBody(DecacCompiler compiler) throws CodeGenError {
        compiler.addComment(" ---------ClassBodyCodeGeneration------");
        listVar.codeGenListDecl(compiler);
        listInstructions.codeGenListInst(compiler);
    }


}
