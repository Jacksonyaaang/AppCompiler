package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    int boolOpIdentifier = 0;
    
    public int getBoolOpIdentifier() {
        return boolOpIdentifier;
    }

    public void setBoolOpIdentifier(int boolOpIdentifier) {
        this.boolOpIdentifier = boolOpIdentifier;
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        throw new CodeGenError("Cette fonction ne doit pas être appeller à ce niveau");
    }
    
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
            throw new ContextualError("Opération compatible qu'avec des boolean",getLocation());
        }

       // if(getLeftOperand().getType().sameType(getLeftOperand().getType()) && getRightOperand().getType().isBoolean() &&)

        return compiler.environmentType.BOOLEAN;
    }


}
