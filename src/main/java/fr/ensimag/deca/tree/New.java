package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
//import net.bytebuddy.jar.asm.Label;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.POP;

public class New  extends AbstractExpr{

    protected AbstractIdentifier className;



    public New(AbstractIdentifier className){
        Validate.notNull(className);
        this.className = className;
    }

    public AbstractIdentifier getClassName() { return className;}

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        int nbattributs = className.getClassDefinition().getNumberOfFields();
        GPRegister Rm=this.LoadGencode(compiler, false);
        compiler.addInstruction(new NEW(nbattributs+1, Rm));
        compiler.addInstruction(new BOV(new Label("heap_overflow_error")));
        compiler.addInstruction(new LEA(className.getClassDefinition().getMethodTableBase(), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), 0(Rm)));
        compiler.addInstruction(new PUSH(Rm));
        compiler.addInstruction(new BSR(init.A));
        //traiter se problème
        compiler.addInstruction(new POP(Rm));
        
        // on stocke l’adresse de a dans l’espace de la pile dédié aux variables        
        // globales, indice l: premier registre libre dans cette partie de la pile
        compiler.addInstruction(new STORE(Rm, l(GB)));
        

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, true);
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);                 
    }
    
}
