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

public class NewTable  extends AbstractExpr{
    
    
    private static final Logger LOG = Logger.getLogger(NewTable.class);

    protected AbstractIdentifier tableType; // we get the matrix dimension for thetype of this element that we set at htis level
    protected ListExpr initializers;

    public NewTable(AbstractIdentifier tableType, ListExpr initializers){
        Validate.notNull(tableType);
        Validate.notNull(initializers);
        this.tableType = tableType;
        this.initializers  = initializers; 
    }

    public AbstractIdentifier getTableType() { return tableType;}
    public ListExpr getInitializers() { return initializers;}


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
            // A  FAIRE
            for (AbstractExpr  expr : initializers.getList()){
                if(!(expr.verifyExpr(compiler, localEnv, currentClass).isInt())){
                    throw new ContextualError("Le type de l'initialisation de la matrice n'est pas un int", getLocation());
                }
            }                                                          
            Type type = tableType.verifyType(compiler);
            if(!type.isTable()){
                throw new ContextualError("Ce type de tableau n'existe pas", getLocation());
            }
            setType(type);
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        // getClassName().decompile(s);
        // s.print("()");
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
        tableType.prettyPrint(s, prefix, false);
        initializers.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        tableType.iter(f);   
        initializers.iter(f);              
    }
    
}



