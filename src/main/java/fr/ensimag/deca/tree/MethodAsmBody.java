package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodAsmBody extends AbstractMethodBody {
    
    protected StringLiteral stringLiteral;

    public MethodAsmBody(StringLiteral stringLiteral) {
        this.stringLiteral = stringLiteral;
    }

    protected String getPrintName(){
        return "println";
    }

    protected String getPrintxName(){
        return "printlnx";
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
