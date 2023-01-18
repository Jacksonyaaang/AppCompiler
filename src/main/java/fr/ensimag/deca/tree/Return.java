package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;


public class Return extends AbstractInst {

    private static final Logger LOG = Logger.getLogger(Return.class);

    protected AbstractExpr exprReturn;

    public Return(AbstractExpr exprReturn){
        Validate.notNull(exprReturn);
        this.exprReturn = exprReturn;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        LOG.debug("[Return][verifyInst]");
        if(returnType.isVoid()){
            throw new ContextualError("Le type de retour de la méthode ne peut être void", getLocation());
        }
        getExprReturn().verifyRValue(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError {
        GPRegister R;
        exprReturn.LoadGencode(compiler, true);
        R=exprReturn.getRegisterDeRetour();
        compiler.addInstruction(new LOAD(R, Register.getR(0)));
        compiler.addInstruction(new BRA(fin....));
        compiler.addInstruction(new WSTR("Erreur : sortie de la methode A.getX sans return"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        getExprReturn().decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        exprReturn.prettyPrint(s, prefix, true);    
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        exprReturn.iter(f);           
    }

    public AbstractExpr getExprReturn() {
        return exprReturn;
}
}
