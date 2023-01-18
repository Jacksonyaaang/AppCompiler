package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class NotEquals extends AbstractOpExactCmp {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    
    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[NotEquals][executeBinaryOperation] Running NotEquals operation " );
        LOG.debug("[NotEquals][executeBinaryOperation] generating code for NotEquals between: " 
            +val + " and " + resultRegister);

        compiler.addInstruction(new CMP(val, resultRegister), "Comparing registers for NotEquals operation ");
        compiler.addInstruction(new SNE(resultRegister), "Placing NotEquals result " +
                                                        "into the register  ");   
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
