package fr.ensimag.deca.context;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.tree.Visibility;

import org.apache.commons.lang.Validate;


public class TableDefinition extends TypeDefinition {
    
    public TableDefinition(Type type, Location location) {
        super(type, location);
        assert(type instanceof TableType == true);
        numberOfFields = ((TableType) type).getDimension();
        assert(numberOfFields<=2 && numberOfFields>=1);
        elementsType = ((TableType) type).getElementsType();
        setUpField();
    }

    private Type elementsType;
    private int numberOfFields = 0;
    public final SymbolTable symbolTableLocal = new SymbolTable();

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }
    
    public EnvironmentExp getMembers() {
        return members;
    }

    @Override
    public boolean isTable() {
        return true;
    }
    // public FieldDefinition(Type type, Location location, Visibility visibility, int index) {



    public void setUpField(){
        Identifier sizeDimension1D= new Identifier(symbolTableLocal.create("size1D"));
        Identifier sizeDimension2D= new Identifier(symbolTableLocal.create("size2D"));
        
        int index1D = 1;
        FieldDefinition fieldDf1D = new FieldDefinition( elementsType, Location.BUILTIN, Visibility.PUBLIC, index1D);
        sizeDimension1D.setDefinition(fieldDf1D);
        try{
            members.declare(sizeDimension1D.getName(), fieldDf1D); 
        } 
        catch(DoubleDefException e ){
            //Should never be the case 
            System.exit(1);
        }
        if (numberOfFields == 2){
            int index2D = 2;
            FieldDefinition fieldDf2D = new FieldDefinition( elementsType, Location.BUILTIN, Visibility.PUBLIC, index2D);
            sizeDimension2D.setDefinition(fieldDf2D);
            try{
                members.declare(sizeDimension2D.getName(), fieldDf2D); 
            } 
            catch(DoubleDefException e ){
                //Should never be the case 
                System.exit(1);
            }
        }



    }

    
    private final EnvironmentExp members = new EnvironmentExp(null);

    public Type getElementsType() {
        return elementsType;
    }

}

// /* */
// package fr.ensimag.deca.context;

// import fr.ensimag.deca.tree.Location;
// import fr.ensimag.ima.pseudocode.Label;
// import org.apache.commons.lang.Validate;

// /**
//  * Definition of a class.
//  *
//  * @author gl15
//  * @date 01/01/2023
//  */
// public class ClassDefinition extends TypeDefinition {


//     public void setNumberOfFields(int numberOfFields) {
//         this.numberOfFields = numberOfFields;
//     }

//     public int getNumberOfFields() {
//         return numberOfFields;
//     }

//     public void incNumberOfFields() {
//         this.numberOfFields++;
//     }

//     public int getNumberOfMethods() {
//         return numberOfMethods;
//     }

//     public void setNumberOfMethods(int n) {
//         Validate.isTrue(n >= 0);
//         numberOfMethods = n;
//     }
    
//     public int incNumberOfMethods() {
//         numberOfMethods++;
//         return numberOfMethods;
//     }

//     private int numberOfFields = 0;
//     private int numberOfMethods = 0;
    
//     @Override
//     public boolean isClass() {
//         return true;
//     }
    
//     @Override
//     public ClassType getType() {
//         // Cast succeeds by construction because the type has been correctly set
//         // in the constructor.
//         return (ClassType) super.getType();
//     };

//     public ClassDefinition getSuperClass() {
//         return superClass;
//     }

//     private final EnvironmentExp members;
//     private final ClassDefinition superClass; 



//     public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
//         super(type, location);
//         EnvironmentExp parent;
//         if (superClass != null) {
//             parent = superClass.getMembers();
//         } else {
//             parent = null;
//         }
//         members = new EnvironmentExp(parent);
//         this.superClass = superClass;
//     }
    
// }

