package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.Label;


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
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Division][executeBinaryOperation] Running Division operation " );
        if (!getWorkWithFloats()){
            assert(val instanceof GPRegister);
            DVal literal0 = new ImmediateInteger(0);
            compiler.addInstruction(new CMP(literal0, (GPRegister)(val) ));                                            
            compiler.addInstruction(new BEQ(new Label("div0_error")), "Checking for division by 0"
                                        +"the operation is between two ints ");
            compiler.getErrorManagementUnit().activeError("div0_error");
            compiler.addInstruction(new QUO(val, resultRegister));
        }
        else{
            compiler.addInstruction(new DIV(val, resultRegister));
            compiler.addInstruction(new BOV(new Label("overflow_error")), "Checking for overflow since "
                                                                     +"the operation is between two floats ");
            compiler.getErrorManagementUnit().activeError("overflow_error");
        }
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
