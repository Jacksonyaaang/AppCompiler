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
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;
import org.apache.log4j.Logger;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;
    private static final Logger LOG = Logger.getLogger(IfThenElse.class);
    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        LOG.debug("[IfThenElse][CodeGenInst] generating code for IfThenElse");
        System.out.println("[IfThenElse][codeGenInst] generating code for IfThenElse");
        System.out.println(compiler.getRegisterManagement());
        Label while_begin=new Label("while_begin");
        compiler.addLabel(while_begin);
        this.condition.codeGenInst(compiler);
        GPRegister Rret = this.condition.getRegisterDeRetour();
        this.body.codeGenListInst(compiler);
        compiler.addInstruction(new CMP(1,Rret));
        compiler.addInstruction(new BEQ(while_begin));
    }

    public void loadItemintoRegister(DecacCompiler compiler, GPRegister regReserved) throws CodeGenError {
        throw new CodeGenError("[AbstractExpr] This method should not be called at this level, loadItemintoRegister");
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
            System.out.println("On est dans While.java");
            condition.verifyCondition(compiler, localEnv, currentClass);
            body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
