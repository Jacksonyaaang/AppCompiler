package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl15
 * @date 01/01/2023
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));
                
        Symbol intTableSymb1D = compiler.createSymbol("int[]");
        TABLEINT1D = new TableType(intTableSymb1D, INT, 1);
        envTypes.put(intTableSymb1D, new TableDefinition(TABLEINT1D, Location.BUILTIN));

        Symbol intTableSymb2D = compiler.createSymbol("int[][]");
        TABLEINT2D = new TableType(intTableSymb2D, INT, 2);
        envTypes.put(intTableSymb2D, new TableDefinition(TABLEINT2D, Location.BUILTIN));


        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

<<<<<<< HEAD
        Symbol floatTableSymb1D = compiler.createSymbol("float[]");
        TABLEFLOAT1D = new TableType(floatTableSymb1D, FLOAT, 1);
        envTypes.put(floatTableSymb1D, new TableDefinition(TABLEFLOAT1D, Location.BUILTIN));
=======
        Symbol flaotTableSymb1D = compiler.createSymbol("float[]");
        TABLEFLAOT1D = new TableType(flaotTableSymb1D, FLOAT, 1);
        envTypes.put(flaotTableSymb1D, new TableDefinition(TABLEFLAOT1D, Location.BUILTIN));
>>>>>>> origin/extention_operations_Stev

        Symbol floatTableSymb2D = compiler.createSymbol("float[][]");
        TABLEFLOAT2D = new TableType(floatTableSymb2D, FLOAT, 2);
        envTypes.put(floatTableSymb2D, new TableDefinition(TABLEFLOAT2D, Location.BUILTIN));
        

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));


        Symbol booleanTableSymb1D = compiler.createSymbol("boolean[]");
        TABLEBOOLEAN1D = new TableType(booleanTableSymb1D, BOOLEAN, 1);
        envTypes.put(booleanTableSymb1D, new TableDefinition(TABLEBOOLEAN1D, Location.BUILTIN));

        Symbol booleanTableSymb2D = compiler.createSymbol("boolean[][]");
        TABLEBOOLEAN2D = new TableType(booleanTableSymb2D, BOOLEAN, 2);
        envTypes.put(booleanTableSymb2D, new TableDefinition(TABLEBOOLEAN2D, Location.BUILTIN));

        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(nullSymb, new TypeDefinition(NULL, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.
        

        /*
         * Ajoute de la classe object avec sa méthode equals
         */
        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb);
        ClassDefinition defintionClassObject = new ClassDefinition(OBJECT, Location.BUILTIN, null);
        OBJECT.setDefinition(defintionClassObject);
        envTypes.put(objectSymb, defintionClassObject);
        
        
        Symbol objectTableSymb1D = compiler.createSymbol("Object[]");
        TABLEOBJECT1D = new TableType(objectTableSymb1D, OBJECT, 1);
        envTypes.put(objectTableSymb1D, new TableDefinition(TABLEOBJECT1D, Location.BUILTIN));

        Symbol objectTableSymb2D = compiler.createSymbol("Object[][]");
        TABLEOBJECT2D = new TableType(objectTableSymb2D, OBJECT, 2);
        envTypes.put(objectTableSymb2D, new TableDefinition(TABLEOBJECT2D, Location.BUILTIN));

        //On défnit ici la méthode interne à la class object : la méthode equals qui 
        // compare deux instances de Object 
        Symbol equalsMethodObject = compiler.createSymbol("equals");
        Signature signature = new Signature();
        signature.add(OBJECT);
        MethodDefinition defintionMethodEquals = new MethodDefinition(BOOLEAN, Location.BUILTIN, signature, defintionClassObject.incNumberOfMethods());
        defintionMethodEquals.setMethodname("equals");
        defintionMethodEquals.setLabel(new Label("code.Object.equals"));
        try {
            defintionClassObject.getMembers().declare(equalsMethodObject, defintionMethodEquals);
        } catch (DoubleDefException e) {
            //C'est impossible d'être dans cette position car la méthode equals ne peux par 
            // être défini deux fois
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void addTableClassType(DecacCompiler compiler, Symbol name, ClassType newClassType){
        Symbol objectTableSymb1D = compiler.createSymbol(name.getName() + "[]");
        TableType NewClassTable1D = new TableType(objectTableSymb1D, newClassType, 1);
        envTypes.put(objectTableSymb1D, new TableDefinition(NewClassTable1D, Location.BUILTIN));

        Symbol objectTableSymb2D = compiler.createSymbol(name.getName() + "[][]");
        TableType NewClassTable2D = new TableType(objectTableSymb2D, newClassType, 2);
        envTypes.put(objectTableSymb2D, new TableDefinition(NewClassTable2D, Location.BUILTIN));
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public Map<Symbol, TypeDefinition> getEnvTypes(){
        return envTypes;
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final TableType    TABLEINT1D;
    public final TableType    TABLEINT2D;
    public final FloatType   FLOAT;
    public final TableType    TABLEFLOAT1D;
    public final TableType    TABLEFLOAT2D;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final TableType    TABLEBOOLEAN1D;
    public final TableType    TABLEBOOLEAN2D;
    public final NullType NULL;
    public final ClassType OBJECT;
    public final TableType    TABLEOBJECT1D;
    public final TableType    TABLEOBJECT2D;
}