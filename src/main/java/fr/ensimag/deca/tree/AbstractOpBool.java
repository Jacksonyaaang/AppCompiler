package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        System.out.println("On est dans AbstractOpBool.java");
        getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
        getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));

        System.out.println(getLeftOperand().getType().getName().getName());
        System.out.println(getRightOperand().getType().getName().getName());

        System.out.println( getRightOperand().getType() instanceof  BooleanType);
        System.out.println( getLeftOperand().getType() instanceof  BooleanType);
        if (!(getLeftOperand().getType() instanceof BooleanType && getRightOperand().getType() instanceof  BooleanType)) {
            System.out.println("ayfeZKUGLAEIHK");
                throw new ContextualError("Opération compatible qu'avec des boolean",getLocation());
            }

       // if(getLeftOperand().getType().sameType(getLeftOperand().getType()) && getRightOperand().getType().isBoolean() &&)

        return compiler.environmentType.BOOLEAN;
    }

}
