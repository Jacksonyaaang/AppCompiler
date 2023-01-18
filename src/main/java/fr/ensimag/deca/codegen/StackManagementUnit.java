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

import fr.ensimag.deca.DecacCompiler;

public class StackManagementUnit {
    
    /**
     * @gbCounter save la valeur qu'on a atteint pour le offset du registre gb
     * @lbCounter stocke la valeur qu'on a atteint pour les variables de methodes
     * @stacksizeNeededMain determine la taille maximal du stack dont on aure besoin pour le main program
     * @opBoolIncrementer associe à chaque operation boolean de type AND Ou OR un identifiant spécifque 
     * @ifIncrementer associe à chaque ifthenelse bloc un identifiant spécifque 
     * @whileIncrementer associe à chaque boule un identifiant spécifque 
     */
    private int gbCounter = 0;
    private int lbCounter = 0; 
    // Valeur de paramCounter mis à 2 car le lb counter sera utilisée dans les methodes
    //et les deux premier espaces dnas le lb counter seront utilisées pour stocker l'adresse de retour
    //et l'adresse de la class actuel   
    private int paramCounter = -2; 
    private int stackSizeNeededMain = 0;
    private int stackSizeNeededMethod = 0;
    

    public int getGbCounter() {
        return gbCounter;
    }
    public int incrementGbCounter() {
        gbCounter++;
        return gbCounter;
    }


    public int getStacksizeNeededMain() {
        return stackSizeNeededMain;
    }

    public int measureStacksizeNeededMain(DecacCompiler compiler) {
        stackSizeNeededMain =  gbCounter + compiler.getRegisterManagement().getMaxTempVariables();
        return stackSizeNeededMain;
    }

    public int measureStacksizeNeededMethod(DecacCompiler compiler) {
        stackSizeNeededMethod =  lbCounter + compiler.getRegisterManagement().getMaxTempVariables() + compiler.getRegisterManagement().numberOfRegisterUsedInMethod();
        return stackSizeNeededMethod;
    }

    public int getLbCounter() {
        return lbCounter;
    }
    public void setLbCounter(int lbCounter) {
        this.lbCounter = lbCounter;
    }
    
    public int incrementLbCounter() {
        lbCounter++;
        return lbCounter;
    }

    public int getParamCounter() {
        return paramCounter;
    }
    public void setParamCounter(int paramCounter) {
        this.paramCounter = paramCounter;
    }
    
    public int decrementParamCounter() {
        paramCounter--;
        return paramCounter;
    }
}
