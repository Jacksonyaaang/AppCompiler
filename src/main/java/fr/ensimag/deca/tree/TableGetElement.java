package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.TableDefinition;
import fr.ensimag.deca.context.TableType;
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

public class  TableGetElement extends AbstractExpr{
    
    
    private static final Logger LOG = Logger.getLogger(TableGetElement.class);

    protected AbstractIdentifier tableIdentifier;
    protected ListExpr initializers;

    public TableGetElement(AbstractIdentifier tableIdentifier, ListExpr initializers){
        Validate.notNull(tableIdentifier);
        Validate.notNull(initializers);
        this.tableIdentifier = tableIdentifier;
        this.initializers  = initializers; 
    }

    public AbstractIdentifier getTableIdentifier() { return tableIdentifier;}
    public ListExpr getInitializers() { return initializers;}


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
            for (AbstractExpr  expr : initializers.getList()){
                if(!(expr.verifyExpr(compiler, localEnv, currentClass).isInt())){
                    throw new ContextualError("Le type de l'acces aux element d'une matrice doit être un int", getLocation());
                }
            }     
            Type tableType = tableIdentifier.verifyExpr(compiler, localEnv, currentClass);          
            if(tableType.isTable()){
                throw new ContextualError("Ce type de tableau n'existe pas", getLocation());
            }
            TableType tableTypeElement = (TableType) tableType;
            setType(tableTypeElement);
            if(tableTypeElement.getDimension() == initializers.size()){
                throw new ContextualError("Le tableau est de dimension = " + tableTypeElement.getDimension(), getLocation());
            }
            return tableTypeElement.getElementsType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getTableIdentifier().decompile(s);
        for (AbstractExpr  expr : initializers.getList()){
            s.print("[");
            expr.decompile();
            s.print("]");
        }  
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        // LOG.debug("[New][codeGenInst] generating new for the class = " + className.getName());
        // this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        // assert(reg != null);
        // compiler.getRegisterManagement().increaseTempVariables(3);
        // compiler.addComment("--------StartNew--------"+getLocation()+"-----");
        // LOG.debug("[New][loadItemintoRegister] loading new of calss =  "+ className.getName()+ " into memory at register " + reg);
        // compiler.addComment("[New][loadItemintoRegister] loading new of calss =  "+ className.getName()+ " into memory at register " + reg);
        // int nbattributs = className.getClassDefinition().getNumberOfFields();
        // //On reserve suffisament d'espace pour les registers et l'adresse de la table de method
        // compiler.addInstruction(new NEW(nbattributs+1, reg));
        // if (!(compiler.getCompilerOptions().isNoCheck())){
        //     compiler.addInstruction(new BOV(new Label("heap_overflow_error")));
        //     compiler.getErrorManagementUnit().activeError("heap_overflow_error");
        // }
        // compiler.addInstruction(new LEA(compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(className.getClassDefinition()), Register.getR(0)));
        // compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(0, reg)));
        // //les instructions de Push and pop ne sont pas necessaires car dans la méthode de init 
        // // on push et pop tout les registres qui ne sont pas stables
        // compiler.addInstruction(new PUSH(reg));
        // compiler.addInstruction(new BSR(new Label("init."+((Identifier) className).getName())));
        // compiler.addInstruction(new POP(reg));
        // compiler.getRegisterManagement().decreaseTempVariables(3); 
        // // on stocke l’adresse de a dans l’espace de la pile dédié aux variables        
        // // globales ou locales , indice l: premier registre libre dans cette partie de la pile
        // compiler.addComment("--------EndNew--------"+getLocation()+"-----");
    }



    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        tableIdentifier.prettyPrint(s, prefix, false);
        initializers.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        tableIdentifier.iter(f);   
        initializers.iter(f);              
    }
    
}




