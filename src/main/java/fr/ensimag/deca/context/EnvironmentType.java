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

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));
        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(booleanSymb, new TypeDefinition(NULL, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.
        
        Symbol objectSymb = compiler.createSymbol("object");
        OBJECT = new ClassType(objectSymb);
        ClassDefinition defintionClassObject = new ClassDefinition(OBJECT, Location.BUILTIN, null);
        envTypes.put(objectSymb, defintionClassObject);
        //On défnit ici la méthode interne à la class object : la méthode equals qui 
        // compare deux instances de Object 
        Symbol equalsMethodObject = compiler.createSymbol("equals");
        Signature signature = new Signature();
        signature.add(OBJECT);
        MethodDefinition defintionMethodEquals = new MethodDefinition(VOID, Location.BUILTIN, signature, defintionClassObject.incNumberOfMethods());
        defintionMethodEquals.setMethodname("equals");
        defintionMethodEquals.setLabel(new Label("code.object.equals"));
        try {
            defintionClassObject.getMembers().declare(equalsMethodObject, defintionMethodEquals);
        } catch (DoubleDefException e) {
            //C'est impossible d'être dans cette position car la méthode equals ne peux par 
            // être défini deux fois
            e.printStackTrace();
            System.exit(1);
        }
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
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final NullType NULL;
    public final ClassType OBJECT;
}