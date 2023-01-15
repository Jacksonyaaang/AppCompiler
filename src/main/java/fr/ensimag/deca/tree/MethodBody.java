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
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        
    }
}
