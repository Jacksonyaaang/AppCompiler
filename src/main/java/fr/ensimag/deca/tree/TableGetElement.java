package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

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
import fr.ensimag.ima.pseudocode.RegisterIndirectOffset;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
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
        compiler.addComment("--------StartGetTableElements--------"+getLocation()+"-----");
        LOG.debug("[TableGetElement][codeGenInst] getting elements from the table" + tableIdentifier.getName());
        this.setRegisterDeRetour(this.LoadGencode(compiler, true));
    }

    @Override
    public void loadItemintoRegister(DecacCompiler compiler, GPRegister reg)  throws CodeGenError{
        assert(reg != null);
        LOG.debug("[TableGetElement][loadItemintoRegister] loading TableGetElement  =  "+ tableIdentifier.getName()+ " into memory at register " + reg);
        compiler.addComment("[TableGetElement][loadItemintoRegister] loading TableGetElement  =  "+ tableIdentifier.getName()+ " into memory at register " + reg);
        /*
         *  On calcule les indices donnée par l'utilisateur
         *  et on les met dans des registre, et on vérifie que c'est valeur
         * sont supérieur ou égal à 0
         */
        ArrayList<AbstractExpr> listExprInit = new ArrayList<AbstractExpr>();
        for (AbstractExpr  expr : initializers.getList()){
            listExprInit.add(expr);
            expr.codeGenInst(compiler);
            //Ajoute de 1 pour avoir le correct index
            verifyExprIsPositive(compiler, expr);
        }
        /**
         * On met l'adresse de la matrice dans un registre qui est dans ce cas le registre:
         * (R1 -> stocke l'adresse)
         */
        compiler.addInstruction(new LOAD(tableIdentifier.getExpDefinition().getOperand(), Register.getR(1)),
            "loading "+tableIdentifier.getName()+ " into memory");
        /**
         * On vérifie que l'adresse de la matrice n'est pas nulle
         */
        if (!compiler.getCompilerOptions().isNoCheck()){
            compiler.addInstruction(new CMP(new NullOperand(), Register.getR(1)), null);
            compiler.addInstruction(new BEQ(new Label("deref_null_error")), 
                                    "Checking if the class identifier is null");
            compiler.getErrorManagementUnit().activeError("deref_null_error");
        }
        /**
         * On vérifie que les indices donnée par l'utilisateur sont inférieur aux dimension 
         * de la matrice
         */
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(1)), Register.getR(0)),
            "loading size 1d of "+tableIdentifier.getName()+ " into memory");
        verifyExprIsLowerThenRegister(compiler, listExprInit.get(0), Register.getR(0));

        if (initializers.size() == 1){
            //Ajout de 1 pour ce positionner correctement, par rapport à la matrice
            compiler.addInstruction(new LOAD(new RegisterIndirectOffset(1, Register.getR(1), listExprInit.get(0).getRegisterDeRetour()), Register.getR(0)));
        }
        else if(initializers.size() == 2){
            //On vérifie que la valeur donnée pour la dimension 2 est inférieur à la taille de la matrice
            compiler.addInstruction(new LOAD(new RegisterOffset(1, Register.getR(1)), Register.getR(0)),
            "loading size 2d of "+tableIdentifier.getName()+ " into memory");
            verifyExprIsLowerThenRegister(compiler, listExprInit.get(1), Register.getR(0));
            //On se positionne corectement dans la matrice en multipliant la ligne par la taille de la colonne
            compiler.addInstruction(new MUL(Register.getR(0), listExprInit.get(0).getRegisterDeRetour()));
            //On ajoute la valeur des colonnes à l'indice
            compiler.addInstruction(new ADD(listExprInit.get(1).getRegisterDeRetour(), listExprInit.get(0).getRegisterDeRetour()));
            /**
             * Pour récupére les élements de la matrice, il faut avoir le bon index. On commener à faire la multiplication la valeur de la ligne donnée par la taille de la ligne
             * et on ajoute la colonne donée. Enfin on se positionne corecteemnt par rapport à la taille de lam matrice  
             */
            compiler.addInstruction(new LOAD(new RegisterIndirectOffset(2, Register.getR(1), listExprInit.get(0).getRegisterDeRetour()), Register.getR(0)));
        }
        Collections.reverse(listExprInit);
        for (AbstractExpr  expr : listExprInit ){
            expr.popRegisters(compiler);
            compiler.getRegisterManagement().decrementOccupationRegister(expr.getRegisterDeRetour());
        }   
        /**On met la valeur récupére dans un registre de retour */
        compiler.addInstruction(new LOAD(Register.getR(0), reg));
        compiler.addComment("--------EndGetTableElements--------"+getLocation()+"-----");
    }

    /**
     * Stocke dans l'adresse selectionée, la valeur qui est dans le registre 
     * de l'expression donnée en paramétre.
     * @param compiler
     * @param reg
     * @throws CodeGenError
     */
    public void saveRegsiterIntoAdress(DecacCompiler compiler, AbstractExpr exprRightOperand)  throws CodeGenError{
        assert(exprRightOperand != null);
        compiler.addComment("--------StartStoreTableElements--------"+getLocation()+"-----");
        LOG.debug("[TableGetElement][loadItemintoRegister] storing into TableGetElement  =  "+ tableIdentifier.getName()+ " the value in " + exprRightOperand.getRegisterDeRetour());
        compiler.addComment("[TableGetElement][loadItemintoRegister] storing into TableGetElement  =  "+ tableIdentifier.getName()+ " the value in " + exprRightOperand.getRegisterDeRetour());

        /*
         *  On calcule les indices donnée par l'utilisateur
         *  et on les met dans des registre, et on vérifie que c'est valeur
         * sont supérieur ou égal à 0
         */
        ArrayList<AbstractExpr> listExprInit = new ArrayList<AbstractExpr>();
        for (AbstractExpr  expr : initializers.getList()){
            listExprInit.add(expr);
            expr.codeGenInst(compiler);
            //Ajoute de 1 pour avoir le correct index
            verifyExprIsPositive(compiler, expr);
        }
        /**
         * On met l'adresse de la matrice dans un registre qui est dans ce cas le registre R1
         */
        compiler.addInstruction(new LOAD(tableIdentifier.getExpDefinition().getOperand(), Register.getR(1)),
            "[saving] loading "+tableIdentifier.getName()+ " into memory");
        /**
         * On vérifie que l'adresse de la matrice n'est pas nulle
         */
        if (!compiler.getCompilerOptions().isNoCheck()){
            compiler.addInstruction(new CMP(new NullOperand(), Register.getR(1)), null);
            compiler.addInstruction(new BEQ(new Label("deref_null_error")), 
                                    "Checking if the class identifier is null");
            compiler.getErrorManagementUnit().activeError("deref_null_error");
        }
        /**
         * On vérifie que les indices donnée par l'utilisateur sont inférieur aux dimension 
         * de la matrice on vérifie donc que 0(R1) < Rx 
         */
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(1)), Register.getR(0)),
            "loading size 1d of "+tableIdentifier.getName()+ " into memory");
        verifyExprIsLowerThenRegister(compiler, listExprInit.get(0), Register.getR(0));

        if (initializers.size() == 1){
            //Ajout de 1 pour ce positionner corectement, par rapport à la matrice
            compiler.addInstruction(new LOAD(listExprInit.get(0).getRegisterDeRetour(), Register.getR(0)));
            listExprInit.get(0).popRegisters(compiler);
            compiler.getRegisterManagement().decrementOccupationRegister(listExprInit.get(0).getRegisterDeRetour());
            compiler.addInstruction(new STORE(exprRightOperand.getRegisterDeRetour(), new RegisterIndirectOffset(1, Register.getR(1), Register.getR(0))));
        }
        else if(initializers.size() == 2){
            //On vérifie que la valeur donnée pour la dimension 2 est inférieur à la taille de la matrice
            compiler.addInstruction(new LOAD(new RegisterOffset(1, Register.getR(1)), Register.getR(0)),
            "loading size 2d of "+tableIdentifier.getName()+ " into memory");
            verifyExprIsLowerThenRegister(compiler, listExprInit.get(1), Register.getR(0));
                //On se positionne corectement dans la matrice en multipliant la ligne par la taille de la colonne
            compiler.addInstruction(new MUL(Register.getR(0), listExprInit.get(0).getRegisterDeRetour()));
            //On ajoute la valeur des colonnes à l'indice
            compiler.addInstruction(new ADD(listExprInit.get(1).getRegisterDeRetour(), listExprInit.get(0).getRegisterDeRetour()));

            compiler.addInstruction(new LOAD(listExprInit.get(0).getRegisterDeRetour(), Register.getR(0)));
            Collections.reverse(listExprInit);
            for (AbstractExpr  expr : listExprInit){
                expr.popRegisters(compiler);
                compiler.getRegisterManagement().decrementOccupationRegister(expr.getRegisterDeRetour());
            }

            compiler.addInstruction(new STORE(exprRightOperand.getRegisterDeRetour(), new RegisterIndirectOffset(2, Register.getR(1), Register.getR(0))), "Selecting the elemnt stored in 2(R1, R0)");
        
        }
        compiler.addComment("--------EndStoreTableElements--------"+getLocation()+"-----");
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




