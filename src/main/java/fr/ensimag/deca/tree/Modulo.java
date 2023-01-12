package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
            getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
            if (!getLeftOperand().getType().isInt() || !getRightOperand().getType().isInt())
                throw new ContextualError("les operandes pour Modlo doivent être des entiers", getLocation());
        //throw new UnsupportedOperationException("not yet implemented");
            setType(compiler.environmentType.INT);
            return getType();
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
