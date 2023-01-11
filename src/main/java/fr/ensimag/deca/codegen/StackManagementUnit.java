/**
 * Utilities used for IMA code generation.
 *
 * The code generation itself is implement as methods of the {@link fr.ensimag.deca.tree.Tree}
 * class.
 *
 * @author gl15
 * @date 01/01/2023
 */
package fr.ensimag.deca.codegen;


public class StackManagementUnit {
    
    //cette valeur est utilisée pour la classe
    private int gbCounter = 0;
    private int stacksizeNeeded = 0;
    
    public int getGbCounter() {
        return gbCounter;
    }
    public int incrementGbCounter() {
        gbCounter++;
        return gbCounter;
    }

    public int getStacksizeNeeded() {
        return stacksizeNeeded;
    }
    public int incrementStacksizeNeeded() {
        stacksizeNeeded++;
        return stacksizeNeeded;
    }
}
