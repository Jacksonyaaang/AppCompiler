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
    
    @Override
    public boolean isTable() {
        return true;
    }
    // public FieldDefinition(Type type, Location location, Visibility visibility, int index) {




    
    private final EnvironmentExp members = new EnvironmentExp(null);

    public Type getElementsType() {
        return elementsType;
    }

}
