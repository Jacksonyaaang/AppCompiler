package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Full if/else if/else statement.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class IfThenElse extends AbstractInst {
    
    private static final Logger LOG = Logger.getLogger(IfThenElse.class);
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;
    private int identifier;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass, Type returnType)
        throws ContextualError {
        LOG.debug("[IfThenElse][verifyInst] Verify the condition and the instructions of the if then else");
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        identifier = compiler.getStackManagement().incrementIfIncrementer();
        LOG.debug("[IfThenElse][CodeGenInst] generating code for IfThenElse");
        this.condition.codeGenInst(compiler);
        Label elseLab = new Label("else"+identifier);
        Label endLab = new Label("end_ifthenelse_"+identifier);
        compiler.addComment("---------Startif-----------"+ getLocation());
        GPRegister Rret = this.condition.getRegisterDeRetour();
        /*On élimine les regitres qu'on doit pop de l'expr de condition 
        car elle ne seront pas utilisée plus tard*/
        this.condition.emptyRegisterToPop();
        compiler.addInstruction(new CMP(1,Rret), "Comparing expr output in the ifthenelse");
        compiler.addInstruction(new BNE(elseLab));
        this.thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(endLab));
        compiler.addLabel(elseLab);
        this.elseBranch.codeGenListInst(compiler);
        compiler.addLabel(endLab);
        compiler.addComment("---------Endif-----------");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        getCondition().decompile(s);

        s.println("){");

        s.indent();
        getThenBranch().decompile(s);
        s.unindent();

        s.println("}");
        s.println("else {");

        s.indent();
        getElseBranch().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }

    public AbstractExpr getCondition() { return condition; }

    public ListInst getThenBranch() { return thenBranch; }

    public ListInst getElseBranch() {
        return elseBranch;
    }

    public void setElseBranch(ListInst elseBranch) {
        this.elseBranch = elseBranch;
    }
}
