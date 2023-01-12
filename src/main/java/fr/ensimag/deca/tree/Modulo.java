package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

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
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultregister) throws CodeGenError {
        LOG.debug("[Modulo][executeBinaryOperation] Running modulo operation " );
        System.out.println("[Modulo][executeBinaryOperation] generating code for modulo between: " 
                                            +val + " and " + resultregister);
        if (getConvNeeded()){
            addConvertInstructions(compiler);
        }
        if (!getWorkWithFloats()){
            compiler.addInstruction(new REM(val, resultregister));
        }
        else{
            //A FAIRE, VERIFIE S'IL Y A UNE CHOSE A FAIRE POUR LES FLOAT
            compiler.addInstruction(new REM(val, resultregister));
        }
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
