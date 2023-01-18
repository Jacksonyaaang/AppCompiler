package fr.ensimag.deca.tree;

import static org.mockito.Mockito.never;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
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
        ExpDefinition Defi;
        Type t = obj.verifyExpr(compiler, localEnv, currentClass);
        if (t.isClass()){
            EnvironmentExp tmpEnv = ((ClassType)t).getDefinition().getMembers();
            Identifier methodIdent = (Identifier) methode;
            for (; tmpEnv != null; tmpEnv = tmpEnv.getParent()){
                if (!tmpEnv.getExp().containsKey(methodIdent.getName()) && tmpEnv.getParent() == null)
                    throw new ContextualError("L'identificateur " + methodIdent.getName().getName() + " n'est pas défini",getLocation());
                else if (tmpEnv.getExp().containsKey(methodIdent.getName())) break;
            }
            Defi = tmpEnv.get(methodIdent.getName());
            methodIdent.setDefinition(Defi);
            if (Defi.isMethod()){
                //verification des signatures
                if (listParam.getList().size() == ((MethodDefinition)Defi).getSignature().size()){
                    int compteur = 0;
                    while (compteur++ < listParam.getList().size()){
                        Type tPramCall = listParam.getList().get(compteur).verifyExpr(compiler, localEnv, currentClass);
                        Type tPramMeth = ((MethodDefinition)Defi).getSignature().paramNumber(compteur);
                        if (!tPramCall.sameType(tPramMeth)){
                            throw new ContextualError("Signature de methode non prévu", getLocation());
                        }
                    }
                }else{
                    throw new ContextualError("Signature de methode non prévu", getLocation());
                }
            }else{
                throw new ContextualError("ce identificateur n'est pas defini comme methode", getLocation());
            }
        }else{
            throw new ContextualError("l'appelant de cette methode n'est pas une classe", getLocation());
        }
        setType(Defi.getType());
        return Defi.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        LOG.debug("[MethodCall][decompile] decompile entry");

        if(getObj().isImplicit()){
            LOG.debug("[MethodCall][decompile] ifthenelse condition entry");
        }
        else {
            getObj().decompile(s);
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
