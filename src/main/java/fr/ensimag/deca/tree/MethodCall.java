package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;


public class MethodCall extends AbstractExpr {

    protected AbstractExpr obj;
    protected AbstractIdentifier methode;
    protected ListExpr listParam;



    public  MethodCall(AbstractExpr obj, AbstractIdentifier methode, ListExpr listParam){
        Validate.notNull(obj); 
        Validate.notNull(methode);
        Validate.notNull(listParam);
        this.obj = obj;
        this.methode = methode;
        this.listParam = listParam;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        int nbparam = this.listParam.size();
        GPRegister Rm=this.LoadGencode(compiler, false);
        int indice_method=method.getMethodDefinition().        
        compiler.addInstruction(new ADDSP(nbparam+1));
        compiler.addInstruction(new LOAD(i(GB),Rm));
        compiler.addInstruction(new STORE(Rm, 0(SP)));
        /*
         * ajout des paramètres via l'usage
         * LOAD val, R2
         * STORE R2, -i (SP)
         * i: numéro du paramètre
         */
        compiler.addInstruction(new LOAD(0(SP), Rm));
        compiler.addInstruction(new CMP(null, Rm));
        compiler.addInstruction(new BEQ(new Label("deref_null_error")));
        compiler.addInstruction(new LOAD(0(Rm), Rm));
        compiler.addInstruction(new BSR(indice_method+(Rm)));   
        //comment utiliser BSR?         
        compiler.addInstruction(new SUBSP(nbparam));
    }

    public AbstractIdentifier getMethode() { return methode;}
    
    public AbstractExpr getObj() { return obj;}

    public ListExpr getListParam() { return listParam;}

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
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        methode.prettyPrint(s, prefix, false);
        listParam.prettyPrint(s, prefix, true);          
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        methode.iter(f);
        listParam.iter(f);        
    }
    
}
