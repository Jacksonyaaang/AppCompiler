package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;

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
            LOG.debug("[Modulo][verifyExpr]");
            getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
            getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
            if (!getLeftOperand().getType().isInt() || !getRightOperand().getType().isInt())
                throw new ContextualError("Les opérandes utilisées pour un calcul de modulo doivent être des entiers", getLocation());
            setType(compiler.environmentType.INT);
            return getType();
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        LOG.debug("[Modulo][executeBinaryOperation] Running modulo operation " );

        LOG.debug("[Modulo][executeBinaryOperation] generating code for modulo between: " 
                    +val + " and " + resultRegister);

        DVal literal0 = new ImmediateInteger(0);
        compiler.addInstruction(new LOAD(literal0, Register.getR(0) ));                                            
        compiler.addInstruction(new CMP(val, Register.getR(0) ));                                            
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
