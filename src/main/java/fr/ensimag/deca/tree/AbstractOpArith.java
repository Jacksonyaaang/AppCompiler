package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if(!getLeftOperand().getType().isFloat() || !getLeftOperand().getType().isInt() ||
                !getRightOperand().getType().isFloat() || !getRightOperand().getType().isInt()){
            throw new ContextualError("Incompatible pour les opérations arithmétiques",getLocation());
        } else {
            if (getLeftOperand().getType().isFloat() && getRightOperand().getType().isInt()){
                setRightOperand(new ConvFloat(getRightOperand()));
                return compiler.environmentType.FLOAT;
            }
            else if (getLeftOperand().getType().isInt() && getRightOperand().getType().isFloat()){
                setLeftOperand(new ConvFloat(getLeftOperand()));
                return compiler.environmentType.FLOAT;
            } else if (getLeftOperand().getType().isFloat() && getRightOperand().getType().isFloat()){
                return compiler.environmentType.FLOAT;
            }
        }
        return compiler.environmentType.INT;
    }
}
