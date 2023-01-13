package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class Minus extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Plus][executeBinaryOperation] generating code for int literal value " );
        // System.out.println("[Plus][executeBinaryOperation] generating code for Plus between: " 
        //                 +val + " and " + resultRegister);
        LOG.debug("[Plus][executeBinaryOperation] generating code for Plus between: " 
                            +val + " and " + resultRegister);
        LOG.debug("[Plus][executeBinaryOperation] generating code for Plus between: " 
        +val + " and " + resultRegister);

        if (!getWorkWithFloats()){
            compiler.addInstruction(new SUB(val, resultRegister));
        }
        else{
            compiler.addInstruction(new SUB(val, resultRegister));
            // A FAIRE CALL METHODE THAT WILL ADD OVERFLOW MUL AT THE END
            compiler.addInstruction(new BOV(new Label("overflow_error")), "Checking for overflow since "
                                                                        +"the operation is between two floats ");
            compiler.getErrorManagementUnit().activeError("overflow_error");
            }    
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    
}
