package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
//import net.bytebuddy.jar.asm.Label;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.POP;

public class New  extends AbstractExpr{
    
    private static final Logger LOG = Logger.getLogger(DeclField.class);

    protected AbstractIdentifier className;



    public New(AbstractIdentifier className){
        Validate.notNull(className);
        this.className = className;
    }

    public AbstractIdentifier getClassName() { return className;}

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
            Type t = className.verifyType(compiler);
            if(!t.isClass()){
                throw new ContextualError("L'expression \"new\" est utilisée uniquement pour créer des objets dont le type est un type de classe", getLocation());
            }
            setType(t);
        return t;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        getClassName().decompile(s);
        s.print("()");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        LOG.debug("[New][codeGenInst] generating new for the class = " + className.getName());
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        compiler.getRegisterManagement().increaseTempVariables(3);
        compiler.addComment("--------StartNew--------"+getLocation()+"-----");
        LOG.debug("[New][loadItemintoRegister] loading new of calss =  "+ className.getName()+ " into memory at register " + reg);
        compiler.addComment("[New][loadItemintoRegister] loading new of calss =  "+ className.getName()+ " into memory at register " + reg);
        int nbattributs = className.getClassDefinition().getNumberOfFields();
        //On reserve suffisament d'espace pour les registers et l'adresse de la table de method
        compiler.addInstruction(new NEW(nbattributs+1, reg));
        if (!(compiler.getCompilerOptions().isNoCheck())){
            compiler.addInstruction(new BOV(new Label("heap_overflow_error")));
            compiler.getErrorManagementUnit().activeError("heap_overflow_error");
        }
        compiler.addInstruction(new LEA(compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(className.getClassDefinition()), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(0, reg)));
        //les instructions de Push and pop ne sont pas necessaires car dans la méthode de init 
        // on push et pop tout les registres qui ne sont pas stables
        compiler.addInstruction(new PUSH(reg));
        compiler.addInstruction(new BSR(new Label("init."+((Identifier) className).getName())));
        compiler.addInstruction(new POP(reg));
        compiler.getRegisterManagement().decreaseTempVariables(3); 
        // on stocke l’adresse de a dans l’espace de la pile dédié aux variables        
        // globales ou locales , indice l: premier registre libre dans cette partie de la pile
        compiler.addComment("--------EndNew--------"+getLocation()+"-----");
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


