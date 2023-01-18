package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WNL;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class Println extends AbstractPrint {

    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printlnx)
     */
    public Println(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    protected String getPrintName(){
        return "println";
    }

    protected String getPrintxName(){
        return "printlnx";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        super.codeGenInst(compiler);
        compiler.addInstruction(new WNL());
    }

    @Override
    String getSuffix() {
        return "ln";
    }
}
