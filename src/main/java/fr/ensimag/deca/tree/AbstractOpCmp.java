package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import org.apache.log4j.Logger;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        throw new CodeGenError(getLocation(), "Cette fonction ne doit pas être appeller à ce niveau");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("[AbstractOpCmp][verifyExpr] Verify the comparison expressions");
        //Vérification des expressions des membres de droite et de gauche
        getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
        getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
        
        Type typeOpLeft = getLeftOperand().getType();
        Type typeOpRight = getRightOperand().getType();

        LOG.debug("[AbstractOpCmp][verifyExpr] verify type left operand:" + typeOpLeft.getName().getName());
        LOG.debug("[AbstractOpCmp][verifyExpr] verify type right operand:" + typeOpRight.getName().getName());


        //Si le type des opérandes n'est pas approprié (ni int ni float ni boolean), une ContextualError est envoyée
        if (typeOpLeft.isBoolean() || typeOpRight.isBoolean() ){
            if (!(typeOpRight.isBoolean() && typeOpLeft.isBoolean()))
                throw  new ContextualError("Comparaison incompatible entre " + typeOpRight.getName().getName() + " et " + typeOpLeft.getName().getName(), getLocation());
        }
        // if((!typeOpLeft.isFloat() && !(typeOpLeft).isInt() && !typeOpLeft.isBoolean()) ||
        //     (!typeOpRight.isInt() && !(typeOpRight).isFloat() && !typeOpLeft.isBoolean())
        //     ){
        //     throw new ContextualError("Les opérations de comparaison ne sont compatibles qu'avec des int, des float et des boolean",getLocation());
        // }
        // Conversion de l'opérande droite en float si elle est de tye int et que l'opérande gauche est de type float
        if (typeOpLeft.isFloat() && typeOpRight.isInt()){
            ConvFloat cF = new ConvFloat(getRightOperand());
            cF.verifyExpr(compiler, localEnv, currentClass);
            setRightOperand(cF);
        }
        // Conversion du membre gauche en float si elle est de tye int et que le membre droit est de type float
        else if (typeOpLeft.isInt() && typeOpRight.isFloat()){
            ConvFloat cF = new ConvFloat(getLeftOperand());
            cF.verifyExpr(compiler, localEnv, currentClass);
            setLeftOperand(cF);
        }
        else if (typeOpLeft.isClassOrNull() && typeOpRight.isClassOrNull()){
            if (!getOperatorName().equals("==") && !getOperatorName().equals("!=")){
                throw new ContextualError("comparaison Incompatible", getLocation());
            }
        }
        //si les deux opérandes sont des booléens on passe 
        setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

}
