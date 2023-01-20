package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;


/**
 * Unary expression.
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    private static final Logger LOG = Logger.getLogger(UnaryMinus.class);


    public AbstractExpr getOperand() {
        return operand;
    }
    private AbstractExpr operand;
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }


    protected abstract String getOperatorName();
  
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        getOperand().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{    
        LOG.debug("[AbstractUnaryExpr][codeGenInst] generating code for an UnaryOperation");
        LOG.debug("[AbstractUnaryExpr][codeGenInst] Exploring operand");
        getOperand().codeGenInst(compiler);
        this.addUnaryInstruction(compiler,getOperand().getRegisterDeRetour());
        LOG.debug("[AbstractUnaryExpr]unary General operand" + getOperand().getRegisterDeRetour());
        this.setRegisterDeRetour(getOperand().getRegisterDeRetour());
        this.transferPopRegisters(getOperand().getRegisterToPop());
    }

    public void addUnaryInstruction(DecacCompiler compiler, GPRegister registerDeRetour) throws CodeGenError {
        throw new CodeGenError(getLocation(), "cette methode ne doit pas être invoqué à ce niveau, class name = " + this.getClass().getSimpleName());
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
