package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import org.apache.log4j.Logger;


/**
 * @author gl15
 * @date 01/01/2023
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);
 
    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultregister) throws CodeGenError {
        LOG.debug("[Plus][executeBinaryOperation] generating code for int literal value " );
        System.out.println("[Plus][executeBinaryOperation] generating code for Plus between: " 
                        +val + " and " + resultregister);
        if (getConvNeeded()){
            addConvertInstructions(compiler);
        }
        if (!getWorkWithFloats()){
            compiler.addInstruction(new ADD(val, resultregister));
        }
        else{
            compiler.addInstruction(new ADD(val, resultregister));
            // A FAIRE CALL METHODE THAT WILL ADD OVERFLOW MUL AT THE END
            compiler.addInstruction(new BOV(new Label("overflow_error")), "Checking for overflow since "
                                                                        +"the operation is between two floats ");
        }
    }
    

    @Override
    protected String getOperatorName() {
        return "+";
    }
}
