package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SLE;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class LowerOrEqual extends AbstractOpIneq {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[LowerOrEqual][executeBinaryOperation] Running LowerOrEqual operation " );
        // System.out.println("[LowerOrEqual][executeBinaryOperation] generating code for LowerOrEqual between: " 
        //                                     +val + " and " + resultRegister);
        LOG.debug("[LowerOrEqual][executeBinaryOperation] generating code for LowerOrEqual between: " 
        +val + " and " + resultRegister);
        if (getConvNeeded()){
            addConvertInstructions(compiler);
        }
        compiler.addInstruction(new CMP(val, resultRegister), "Comparing registers for LowerOrEqual operation ");
        compiler.addInstruction(new SLE(resultRegister), "Placing LowerOrEqual result " +
                                                        "into the register  ");   
    }

    @Override
    protected String getOperatorName() {
        return "<=";
    }

}
