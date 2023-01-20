package fr.ensimag.deca.tree;


import fr.ensimag.deca.codegen.CodeGenError;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        throw new CodeGenError(getLocation(), "Cette fonction ne doit pas être appeller à ce niveau");
    }

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


}
