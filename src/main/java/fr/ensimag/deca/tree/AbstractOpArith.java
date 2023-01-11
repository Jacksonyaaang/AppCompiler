package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;

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
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultregister) throws CodeGenError {
        //A FAIRE, check overflow, and create a function that checks types and converts values when needed
        //Look for types of int literals and float literals and variable types  
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) throws ContextualError {
        System.out.println("On est dans AbstractOpArith.java");
        getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
        getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
        if (getLeftOperand().getType().isInt()){
            System.out.println(getLeftOperand().getType().getName().getName());
            System.out.println(getRightOperand().getType().getName().getName());
        }
        if(!getLeftOperand().getType().isFloat() && !((IntType)getLeftOperand().getType()).isInt() &&
                !getRightOperand().getType().isFloat() && !((IntType)getRightOperand().getType()).isInt() &&
                !(getLeftOperand() instanceof AbstractReadExpr) && !(getRightOperand() instanceof AbstractReadExpr)){
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
        }System.out.println("FIN ARTH");
        return compiler.environmentType.INT;
    }
}
