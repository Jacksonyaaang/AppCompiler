package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a class.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        if (otherType instanceof ClassType){
            if (this.getName().getName().equals(((ClassType)otherType).getName().getName()))
            return true;
        }return false;
        //throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        ClassDefinition CurClassDef = this.getDefinition();
        //if (CurClassDef == null) return false;
        while (CurClassDef != null){
            ClassType CurClass = CurClassDef.getType();
            if (CurClass.sameType(potentialSuperClass)) {
                return true;
            }
            CurClassDef = CurClassDef.getSuperClass();    
        }
        return false;
        
        //throw new UnsupportedOperationException("not yet implemented"); 
    }


}

