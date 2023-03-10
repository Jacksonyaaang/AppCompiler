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
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;
    private int identifier;

    private static final Logger LOG = Logger.getLogger(And.class);

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
        identifier = compiler.incrementWhileIncrementer();
        LOG.debug("[While][CodeGenInst] generating code for While");
        Label whileBegin = new Label("while_begin" + identifier);
        Label whileEnd = new Label("while_end"+ identifier);
        compiler.addLabel(whileBegin);
        this.condition.codeGenInst(compiler);
        /*On élimine les regitres qu'on doit pop de l'expr de condition 
        car elle ne seront pas utilisée plus tard*/
        this.condition.emptyRegisterToPop();
        GPRegister Rret = this.condition.getRegisterDeRetour();
        compiler.addInstruction(new CMP(1,Rret));
        compiler.addInstruction(new BNE(whileEnd));
        this.body.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(whileBegin));
        compiler.addLabel(whileEnd);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass, Type returnType)
        throws ContextualError {
        LOG.debug("[While][verifyInst]");
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
