package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            getOperand().setType(getOperand().verifyExpr(compiler, localEnv, currentClass));
            if (!getOperand().getType().isInt() && !getOperand().getType().isFloat())
                throw new ContextualError("Not ne s'applique qu'au boolean ", getLocation())
            setType(compiler.environmentType.BOOLEAN);
            return getType();
        //throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
