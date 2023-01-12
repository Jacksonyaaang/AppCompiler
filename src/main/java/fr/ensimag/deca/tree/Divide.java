package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Divide extends AbstractOpArith {
    
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultregister) throws CodeGenError {
        LOG.debug("[Division][executeBinaryOperation] Running Division operation " );
        System.out.println("[Division][executeBinaryOperation] generating code for Division between: " 
                                            +val + " and " + resultregister);
        if (getConvNeeded()){
            addConvertInstructions(compiler);
        }
        if (!getWorkWithFloats()){
            compiler.addInstruction(new QUO(val, resultregister));
        }
        else{
            compiler.addInstruction(new DIV(val, resultregister));
            // A FAIRE CALL METHODE THAT WILL ADD OVERFLOW MUL AT THE END
            compiler.addInstruction(new BOV(new Label("overflow_error")), "Checking for overflow since "
                                                                     +"the operation is between two floats ");
        }
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
