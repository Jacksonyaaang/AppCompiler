package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;

import org.apache.log4j.Logger;


/**
 * @author gl15
 * @date 01/01/2023
 */
public class Multiply extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultregister) throws CodeGenError {
        LOG.debug("[Multiply][executeBinaryOperation] generating code for int literal value " );
        System.out.println("[Multiply][executeBinaryOperation] generating code for multiply between: " 
                        +val + " and " + resultregister);
        compiler.addInstruction(new MUL(val, resultregister));
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

}
