package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
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
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        throw new CodeGenError("Cette fonction ne doit pas être appeller à ce niveau");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        System.out.println("On est dans AbstractOpCmp.java");
        getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
        getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
//        ((Identifier)getRightOperand()).setDefinition(compiler.environmentType.defOfType(((Identifier)getRightOperand()).getName()));
//        ((Identifier)getLeftOperand()).setDefinition(compiler.environmentType.defOfType(((Identifier)getLeftOperand()).getName()));
//        if (getLeftOperand().getType().isInt()){
//            System.out.println(getLeftOperand().getType().getName().getName());
//            System.out.println(getRightOperand().getType().getName().getName());
//        }

//        if(!getLeftOperand().getType().isFloat() && !((IntType)getLeftOperand().getType()).isInt() &&
//                !getRightOperand().getType().isFloat() && !((IntType)getRightOperand().getType()).isInt() &&
//                !(getLeftOperand() instanceof AbstractReadExpr) && !(getRightOperand() instanceof AbstractReadExpr)){
//            throw new ContextualError("Incompatible pour la comparaison",getLocation());
//        }
        System.out.println(getLeftOperand().getType().getName().getName());
        System.out.println(getRightOperand().getType().getName().getName());
        if((!getLeftOperand().getType().isFloat() && !(getLeftOperand().getType()).isInt()) ||
            (!getRightOperand().getType().isInt() && !(getRightOperand().getType()).isFloat())){
            throw new ContextualError("Incompatible pour la comparaison",getLocation());
        }
        if (getLeftOperand().getType().isFloat() && getRightOperand().getType().isInt()){
            ConvFloat cF = new ConvFloat(getRightOperand());
            cF.verifyExpr(compiler, localEnv, currentClass);
            setRightOperand(cF);
            System.out.println("jsuis bien là float int");
            setType(compiler.environmentType.BOOLEAN);
        }
        else if (getLeftOperand().getType().isInt() && getRightOperand().getType().isFloat()){
            ConvFloat cF = new ConvFloat(getLeftOperand());
            cF.verifyExpr(compiler, localEnv, currentClass);
            setLeftOperand(cF);
            //setLeftOperand(new ConvFloat(getLeftOperand()));
            System.out.println("jsuis bien là int float");
            setType(compiler.environmentType.BOOLEAN);
            //return compiler.environmentType.FLOAT;
        } else if (getLeftOperand().getType().isFloat() && getRightOperand().getType().isFloat()){
            System.out.println("jsuis bien là float float");
            setType(compiler.environmentType.BOOLEAN);
        }
        System.out.println("FIN ARTH");
        return compiler.environmentType.BOOLEAN;
    }

       // return null; // A FAIRE: modifier return selon besoins


}
