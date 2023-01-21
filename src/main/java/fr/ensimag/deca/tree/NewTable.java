package fr.ensimag.deca.tree;


import java.io.PrintStream;
import java.util.ArrayList;

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
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
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
        getTableType().decompile(s);
        for (AbstractExpr  expr : initializers.getList()){
            s.print("[");
            expr.decompile();
            s.print("]");
        }  
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        LOG.debug("[NewTable][codeGenInst] generating space for the table = " + tableType.getName());
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        compiler.addComment("--------StartNewTable--------"+getLocation()+"-----");
        LOG.debug("[NewTable][loadItemintoRegister] loading NewTable  =  "+ tableType.getName()+ " into memory at register " + reg);
        compiler.addComment("[NewTable][loadItemintoRegister] loading NewTable  =  "+ tableType.getName()+ " into memory at register " + reg);
        /*
         *  On calcule le contenu de la dimension et on les met dans un registre 
         */
        ArrayList<AbstractExpr> listExprInit = new ArrayList<AbstractExpr>();
        for (AbstractExpr  expr : initializers.getList()){
            listExprInit.add(expr);
            expr.codeGenInst(compiler);
            verifyExprIsPositive(compiler, expr);
        }   
        if (initializers.size() == 1){
            compiler.addInstruction(new ADD(new ImmediateInteger(1), listExprInit.get(0).getRegisterDeRetour()));
            compiler.addInstruction(new NEW(listExprInit.get(0).getRegisterDeRetour(), Register.getR(0)));
        }
        else if(initializers.size() == 2){
            compiler.addInstruction(new MUL(listExprInit.get(1).getRegisterDeRetour(), listExprInit.get(0).getRegisterDeRetour()));
            compiler.addInstruction(new ADD(new ImmediateInteger(2), listExprInit.get(0).getRegisterDeRetour()));
            compiler.addInstruction(new NEW(listExprInit.get(0).getRegisterDeRetour(), Register.getR(0)));
        }

        //On reserve suffisament d'espace pour les registers et l'adresse de la table de method
        if (!(compiler.getCompilerOptions().isNoCheck())){
            compiler.addInstruction(new BOV(new Label("heap_overflow_error")));
            compiler.getErrorManagementUnit().activeError("heap_overflow_error");
        }
        for (AbstractExpr  expr : initializers.getList()){
            expr.popRegisters(compiler);
            compiler.getRegisterManagement().decrementOccupationRegister(expr.getRegisterDeRetour());
        }   
        compiler.addInstruction(new NEW(Register.getR(0), reg));
        compiler.addComment("--------EndNewTable--------"+getLocation()+"-----");
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



