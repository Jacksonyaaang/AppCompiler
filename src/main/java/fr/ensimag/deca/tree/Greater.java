package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Greater extends AbstractOpIneq {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    
    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Greater][executeBinaryOperation] Running Greater operation " );
        //System.out.println("[Greater][executeBinaryOperation] generating code for Greater between: " 
        //                                    +val + " and " + resultRegister);
        LOG.debug("[Greater][executeBinaryOperation] generating code for Greater between: " 
                                            +val + " and " + resultRegister);

        compiler.addInstruction(new CMP(val, resultRegister), "Comparing registers for Greater operation ");
        compiler.addInstruction(new SGT(resultRegister), "Placing Greater result " +
                                                        "into the register  ");   
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

}
