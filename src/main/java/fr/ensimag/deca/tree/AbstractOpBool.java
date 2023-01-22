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

    private static final Logger LOG = Logger.getLogger(AbstractOpBool.class);

    int boolOpIdentifier = 0;
    
    public int getBoolOpIdentifier() {
        return boolOpIdentifier;
    }

    public void setBoolOpIdentifier(int boolOpIdentifier) {
        this.boolOpIdentifier = boolOpIdentifier;
    }

    @Override
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        throw new CodeGenError(getLocation(), "Cette fonction ne doit pas être appeller à ce niveau");
    }
    
    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("[AbstractOpBool][verifyExpr] Verify the boolean expressions");
        //Vérification des expressions des membres de droite et de gauche
        getRightOperand().setType(getRightOperand().verifyExpr(compiler, localEnv, currentClass));
        getLeftOperand().setType(getLeftOperand().verifyExpr(compiler, localEnv, currentClass));
        //Si les deux opérandes ne sont pas de type boolean, on envoie une ContextualError
        if (getLeftOperand() instanceof AbstractIdentifier && ((Identifier)getLeftOperand()).getExpDefinition().isMethod() ||
            getRightOperand() instanceof AbstractIdentifier && ((Identifier)getRightOperand()).getExpDefinition().isMethod()){
                throw new ContextualError("l'une de membre de l'operation est une méthod", getLocation());
            }
        if (!(getLeftOperand().getType() instanceof BooleanType && getRightOperand().getType() instanceof  BooleanType)) {
            throw new ContextualError("Les opérations booléennes ne sont compatibles qu'avec des boolean",getLocation());
        }
        setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }


}
