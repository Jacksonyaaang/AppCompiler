package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Lower extends AbstractOpIneq {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);
    
    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Lower][executeBinaryOperation] Running Lower operation " );
        // System.out.println("[Lower][executeBinaryOperation] generating code for Lower between: " 
        //                                     +val + " and " + resultRegister);
        LOG.debug("[Lower][executeBinaryOperation] generating code for Lower between: " 
                    +val + " and " + resultRegister);

        compiler.addInstruction(new CMP(val, resultRegister), "Comparing registers for Lower operation ");
        compiler.addInstruction(new SLT(resultRegister), "Placing Lower result " +
                                                        "into the register  ");   
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

}
