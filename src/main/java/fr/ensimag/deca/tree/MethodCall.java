package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

public class MethodCall extends AbstractExpr {

    protected AbstractExpr obj;
    protected AbstractIdentifier methode;
    protected ListExpr listParam;

    private static final Logger LOG = Logger.getLogger(Modulo.class);

    public  MethodCall(AbstractExpr obj, AbstractIdentifier methode, ListExpr listParam){
        Validate.notNull(obj); 
        Validate.notNull(methode);
        Validate.notNull(listParam);
        this.obj = obj;
        this.methode = methode;
        this.listParam = listParam;
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
        LOG.debug("[MethodCall][decompile] decompile entry");
        String str = getObj().decompile();
        s.print(str);
        if(str == ""){
            LOG.debug("[MethodCall][decompile] ifthenelse condition entry");
        }
        else {
            s.print(".");
        }
        getMethode().decompile(s);
        s.print("(");
        getListParam().decompile(s);
        s.print(")");
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
