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

/**
 * Full if/else if/else statement.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;


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
            System.out.println("On est dans IfThenElse.java");
            condition.verifyCondition(compiler, localEnv, currentClass);
            thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
            elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("[IfThenElse][CodeGenInst] generating code for RIfThenElse");
        System.out.println("[IfThenElse][codeGenInst] generating code for IfThenElse");
        System.out.println(compiler.getRegisterManagement());
        this.setRegisterDeRetour(this.LoadGencode(compiler));
        System.out.println("[IfThenElse][codeGenInst] exiting method");
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        if(this.condition.instanceof(Equals)){
            Label elseLab = new Label("else");
            Label endLab = new Label("end");
            compiler.addInstruction(new CMP());
            compiler.addInstruction(new BNQ(elseLab));
            this.thenBranch.codeGenListInst(compiler);
            compiler.addInstruction(new BRA(elseLab));
            compiler.addInstruction(elseLab);
            this.elseBranch.codeGenListInst(compiler);
            compiler.addInstruction(endLab);
        }
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
