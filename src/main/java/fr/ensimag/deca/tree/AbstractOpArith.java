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
import org.apache.log4j.Logger;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        //A FAIRE, check overflow, and create a function that checks types and converts values when needed
        //Look for types of int literals and float literals and variable types  
        throw new CodeGenError("Method should not be called at this level");
    }



    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) throws ContextualError {
        LOG.debug("[AbstractOpArith][verifyExpr] Verify the arithmetic expressions");
        //Vérification des expressions des membres de droite et de gauche
        getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
        getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
        //Si le type des opérandes n'est pas approprié(ni int ni float), une ContextualError est envoyée
        if(!getLeftOperand().getType().isFloat() && !(getLeftOperand().getType()).isInt() ||
                (!getRightOperand().getType().isFloat() && !(getRightOperand().getType()).isInt()) ||
                (!(getLeftOperand() instanceof AbstractReadExpr) && !(getRightOperand() instanceof AbstractReadExpr))){
            throw new ContextualError("Les opérations arithmétiques ne sont compatibles qu'avec des int et des float",getLocation());
        }
        // Conversion de l'opérande droite en float si elle est de tye int et que l'opérande gauche est de type float
        if (getLeftOperand().getType().isFloat() && getRightOperand().getType().isInt()){
            LOG.debug("[AbstractOpArith][verifyExpr] Right operand float conversion");
            ConvFloat cF = new ConvFloat(getRightOperand());
            cF.verifyExpr(compiler, localEnv, currentClass);
            setRightOperand(cF);
            setType(compiler.environmentType.FLOAT);
        }
        // Conversion du membre gauche en float si elle est de tye int et que le membre droit est de type float
        else if (getLeftOperand().getType().isInt() && getRightOperand().getType().isFloat()){
            LOG.debug("[AbstractOpArith][verifyExpr] Left operand float conversion");
            ConvFloat cF = new ConvFloat(getLeftOperand());
            cF.verifyExpr(compiler, localEnv, currentClass);
            setLeftOperand(cF);
            setType(compiler.environmentType.FLOAT);
        }
        // Pas de conversion si les deux opérandes sont de type float
        else if (getLeftOperand().getType().isFloat() && getRightOperand().getType().isFloat()){
            setType(compiler.environmentType.FLOAT);
        }
        else setType(compiler.environmentType.INT);
        return getType();
    }
}
