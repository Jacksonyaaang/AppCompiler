package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 * 
 * @author gl15
 * @date 01/01/2023
 */
public class GreaterOrEqual extends AbstractOpIneq {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[GreaterOrEqual][executeBinaryOperation] Running GreaterOrEqual operation " );
        // System.out.println("[GreaterOrEqual][executeBinaryOperation] generating code for GreaterOrEqual between: " 
        //                                     +val + " and " + resultRegister);
        LOG.debug("[GreaterOrEqual][executeBinaryOperation] generating code for GreaterOrEqual between: " 
            +val + " and " + resultRegister);
        if (getConvNeeded()){
            addConvertInstructions(compiler);
        }
        compiler.addInstruction(new CMP(val, resultRegister), "Comparing registers for GreaterOrEqual operation ");
        compiler.addInstruction(new SGE(resultRegister), "Placing GreaterOrEqual result " +
                                                        "into the register  ");   
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
