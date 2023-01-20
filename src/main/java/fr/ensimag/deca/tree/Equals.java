package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Equals extends AbstractOpExactCmp {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Equals][executeBinaryOperation] Running Equals operation " );
        compiler.addInstruction(new CMP(val, resultRegister), "Comparing registers for Equals operation ");
        compiler.addInstruction(new SEQ(resultRegister), "Placing Equals result " +
                                                       "into the register  ");   
    }
    

    @Override
    protected String getOperatorName() {
        return "==";
    }    
    
}
