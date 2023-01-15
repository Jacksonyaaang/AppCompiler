package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.tools.IndentPrintStream;

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
        this.listVar = listVar;
        this.listInstructions = listInstructions;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        getListVar().decompile(s);
        getListInstructions().decompile(s);
        s.println("}");

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
}
