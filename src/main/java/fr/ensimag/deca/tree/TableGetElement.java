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
import fr.ensimag.deca.context.TableDefinition;
import fr.ensimag.deca.context.TableType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
//import net.bytebuddy.jar.asm.Label;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.POP;

public class  TableGetElement extends AbstractLValue{
    
    
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
            if(!tableType.isTable()){
                throw new ContextualError("Ce type de tableau n'existe pas", getLocation());
            }
            TableType tableTypeElement = (TableType) tableType;
            setType(tableTypeElement);
            if(!(tableTypeElement.getDimension() == initializers.size())){
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
        LOG.debug("[TableGetElement][codeGenInst] getting elements fro mthe table" + tableIdentifier.getName());
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        compiler.addComment("--------StartGetTableElements--------"+getLocation()+"-----");
        LOG.debug("[TableGetElement][loadItemintoRegister] loading TableGetElement  =  "+ tableIdentifier.getName()+ " into memory at register " + reg);
        compiler.addComment("[TableGetElement][loadItemintoRegister] loading TableGetElement  =  "+ tableIdentifier.getName()+ " into memory at register " + reg);
        /*
         *  On calcule le contenu de la dimension et on les met dans un registre 
         */
        ArrayList<AbstractExpr> listExprInit = new ArrayList<AbstractExpr>();
        for (AbstractExpr  expr : initializers.getList()){
            listExprInit.add(expr);
            expr.codeGenInst(compiler);
            //Ajoute de 1 pour avoir le correct index
            compiler.addInstruction(new ADD(new ImmediateInteger(1), expr.getRegisterDeRetour()));
            verifyExprIsPositive(compiler, expr);
        }

        compiler.addInstruction(new LOAD(tableIdentifier.getExpDefinition().getOperand(), Register.getR(1)),
            "loading "+tableIdentifier.getName()+ " into memory");
        if (!compiler.getCompilerOptions().isNoCheck()){
            compiler.addInstruction(new CMP(new NullOperand(), Register.getR(1)), null);
            compiler.addInstruction(new BEQ(new Label("deref_null_error")), 
                                    "Checking if the class identifier is null");
            compiler.getErrorManagementUnit().activeError("deref_null_error");
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(1)), Register.getR(0)),
            "loading size 1d of "+tableIdentifier.getName()+ " into memory");
        verifyExprIsLowerThenRegister(compiler, listExprInit.get(0), Register.getR(0));

        if (initializers.size() == 1){
            //Ajout de 1 pour ce positionner corectement
            new RegisterOffset(0, reg);
            compiler.addInstruction(new ADD(new ImmediateInteger(1), listExprInit.get(0).getRegisterDeRetour()));
            compiler.addInstruction(new NEW(listExprInit.get(0).getRegisterDeRetour(), Register.getR(0)));
            compiler.addInstruction(new LOAD(new RegisterIndirectOffset(), reg));
        }
        else if(initializers.size() == 2){
            compiler.addInstruction(new LOAD(new RegisterOffset(1, Register.getR(1)), Register.getR(0)),
            "loading size 2d of "+tableIdentifier.getName()+ " into memory");
            verifyExprIsLowerThenRegister(compiler, listExprInit.get(1), Register.getR(0));
            compiler.addInstruction(new MUL(listExprInit.get(1).getRegisterDeRetour(), listExprInit.get(0).getRegisterDeRetour()));
            compiler.addInstruction(new ADD(new ImmediateInteger(2), listExprInit.get(0).getRegisterDeRetour()));
            compiler.addInstruction(new NEW(listExprInit.get(0).getRegisterDeRetour(), Register.getR(0)));
        }

        for (AbstractExpr  expr : initializers.getList()){
            expr.popRegisters(compiler);
            compiler.getRegisterManagement().decrementOccupationRegister(expr.getRegisterDeRetour());
        }   
        compiler.addInstruction(new NEW(Register.getR(0), reg));
        compiler.addComment("--------EndGetTableElements--------"+getLocation()+"-----");
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




