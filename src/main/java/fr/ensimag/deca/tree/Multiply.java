package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
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
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Multiply][executeBinaryOperation] generating code for multiply between: " 
                    +val + " and " + resultRegister );
        LOG.debug("[Multiply][executeBinaryOperation] generating code for multiply between: " 
                    +val + " and " + resultRegister);

        if (!getWorkWithFloats()){
            compiler.addInstruction(new MUL(val, resultRegister));
        }
        else{
            compiler.addInstruction(new MUL(val, resultRegister));
            if (!(compiler.getCompilerOptions().isNoCheck())) {
                compiler.addInstruction(new BOV(new Label("overflow_error")), "Checking for overflow since "
                                                                        +"the operation is between two floats ");
                compiler.getErrorManagementUnit().activeError("overflow_error");
            }
        }
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

}
