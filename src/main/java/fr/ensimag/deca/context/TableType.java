package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class TableType extends Type{

    /**
     * @elementsType le type des elements du tableau 
     * @dimension indique si le tableau est 1D ou 2D
     */
    public final Type elementsType;
    public final int dimension;

    /**
     * TableType initialise un tableau qui est caractérise par son nom
     * par exemple float[] ou int[] / le type de ses elements
     * et sa dimension
     * @param name symbol du tableau comme float[] 
     * @param elementsType le type des elements du tableau
     * @param dimension indique si le tableau est 1D ou 2D
     */
    public TableType(Symbol name, Type elementsType, int dimension) {
        super(name);
        assert(dimension >=0);
        assert(!(elementsType instanceof VoidType));
        this.elementsType = elementsType;
        this.dimension = dimension;
    }

    @Override
    public boolean sameType(Type otherType) {
        if (otherType instanceof TableType){
            if (this.elementsType == ((TableType)otherType).elementsType && 
                    this.dimension == ((TableType)otherType).dimension ){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTable() {
        return true;
    }

    
}
