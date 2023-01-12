package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.ima.pseudocode.Label;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Modulo extends AbstractOpArith {

    private static final Logger LOG = Logger.getLogger(Modulo.class);

    
    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Modulo][executeBinaryOperation] Running modulo operation " );
        //System.out.println("[Modulo][executeBinaryOperation] generating code for modulo between: " 
                                //            +val + " and " + resultRegister);
        LOG.debug("[Modulo][executeBinaryOperation] generating code for modulo between: " 
                    +val + " and " + resultRegister);
        if (getConvNeeded()){
            addConvertInstructions(compiler);
        }
        DVal literal0 = new ImmediateInteger(0);
        compiler.addInstruction(new CMP(literal0, resultRegister));                                            
        compiler.addInstruction(new BEQ(new Label("div0_error")), "Checking for modulo by 0 "
                                    +"the operation is between two ints ");
        compiler.getErrorManagementUnit().activeError("div0_error");
        compiler.addInstruction(new REM(val, resultRegister));
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
