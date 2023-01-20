package fr.ensimag.deca.tree;

import static org.mockito.Mockito.never;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;


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

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        /*
         * Configuration initial : reservation de l'espace dans le stack, et modification des paramétre 
         *  qui verifie que le stack a suffisament d'espace 
         */
        int nbParamTotal = this.listParam.size()+1;
        int methodIndex = ((Identifier)methode).getMethodDefinition().getIndex(); 
        compiler.getRegisterManagement().increaseTempVariables(nbParamTotal);    
        compiler.addInstruction(new ADDSP(nbParamTotal));

        /*
         * Emplacement de l'adresse de la class dans un registre
         */
        obj.codeGenInst(compiler);
        compiler.addInstruction(new STORE(obj.getRegisterDeRetour(), new RegisterOffset(0, Register.SP)));
        obj.popRegisters(compiler);
        compiler.getRegisterManagement().decrementOccupationRegister(obj.getRegisterDeRetour());

        /*
         * ajout des paramètres via l'usage
         * LOAD val, R2
         * STORE R2, -i (SP)
         * i: numéro du paramètre
         */
        /*
         * Ce paramétre sera utilisée pour associer des adresses aux paramétre de la méthode
         */
        int positionInStack = -1;
        /*
         * Ajout des paramétre dans le stack
         */
        for  (AbstractExpr expr : listParam.getList()){
            expr.codeGenInst(compiler);
            compiler.addInstruction(new STORE(expr.getRegisterDeRetour(), new RegisterOffset(positionInStack, Register.SP)));
            obj.popRegisters(compiler);
            compiler.getRegisterManagement().decrementOccupationRegister(obj.getRegisterDeRetour());
            positionInStack--;
        }

        /*
         * Appel de method
         */
        GPRegister returnRegister = this.LoadGencode(compiler, false);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), returnRegister));
        if (!compiler.getCompilerOptions().isNoCheck()){
            compiler.addInstruction(new CMP(new NullOperand(), returnRegister));
            compiler.addInstruction(new BEQ(new Label("deref_null_error")));
            compiler.getErrorManagementUnit().activeError("deref_null_error");
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(0, returnRegister), returnRegister));
        compiler.addInstruction(new BSR(new RegisterOffset(methodIndex, returnRegister)));   
        compiler.addInstruction(new SUBSP(nbParamTotal));
        compiler.getRegisterManagement().decreaseTempVariables(nbParamTotal);    
        
        /*
         * Stockage du resulat de la méthode, et l'associer à un registre
         */
        compiler.addInstruction(new LOAD(Register.getR(0), returnRegister));
        this.setRegisterDeRetour(returnRegister);
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
                    while (compteur < listParam.getList().size()){
                        Type tPramCall = listParam.getList().get(compteur).verifyExpr(compiler, localEnv, currentClass);
                        Type tPramMeth = ((MethodDefinition)Defi).getSignature().paramNumber(compteur);
                        if (!tPramCall.sameType(tPramMeth)){
                            throw new ContextualError("Signature de methode non prévu", getLocation());
                        }
                        compteur++;
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
