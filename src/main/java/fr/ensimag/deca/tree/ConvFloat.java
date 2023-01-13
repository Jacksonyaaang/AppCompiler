package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl15
 * @date 01/01/2023
 */
public class ConvFloat extends AbstractUnaryExpr {

    private static final Logger LOG = Logger.getLogger(ConvFloat.class);
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) {
        LOG.debug("[ConvFloat][verifyExpr]");
        setType(compiler.environmentType.FLOAT);
        return getType();
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
