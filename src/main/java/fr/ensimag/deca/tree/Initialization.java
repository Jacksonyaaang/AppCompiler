package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class Initialization extends AbstractInitialization {

    private static final Logger LOG = Logger.getLogger(Initialization.class);


    @Override
    public void codegenInitial(DecacCompiler compiler) throws CodeGenError {
        expression.codeGenInst(compiler);
        assert(expression.getRegisterDeRetour() != null);
        super.setRegistreDeRetour(expression.getRegisterDeRetour());
    }

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("[Initialization][verifyInitialization]");
        //Vérification du membre de droite lors d'une initialisation
        expression = expression.verifyRValue(compiler, localEnv, currentClass, t);
        if (expression instanceof AbstractIdentifier && ((Identifier)expression).getExpDefinition().isMethod()) {
            throw new ContextualError("le membre de droite est un identificateur de method", getLocation());
           // System.out.println("********typeOpLeft est null mec***********************************************");
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        getExpression().decompile(s);

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
