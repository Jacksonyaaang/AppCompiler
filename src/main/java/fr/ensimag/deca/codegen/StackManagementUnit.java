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
     * @gbCounter conteur sur la valeur qu'on a atteint pour le offset du registre gb
     * @stacksizeNeededMain determine la taille maximal du stack dont on aure besoin pour le main program
     * @opBoolIncrementer associe à chaque operation boolean de type AND Ou OR un identifiant spécifque 
     * @ifIncrementer associe à chaque ifthenelse bloc un identifiant spécifque 
     * @whileIncrementer associe à chaque boule un identifiant spécifque 
     */
    private int gbCounter = 0;
    private int stacksizeNeededMain = 0;
    private int opBoolIncrementer = 0;
    private int ifIncrementer = 0;
    private int whileIncrementer = 0;

    public int getGbCounter() {
        return gbCounter;
    }
    public int incrementGbCounter() {
        gbCounter++;
        return gbCounter;
    }

    public int getOpBoolIncrementer() {
        return opBoolIncrementer;
    }
    public int incrementOpBoolIncrementer() {
        opBoolIncrementer++;
        return opBoolIncrementer;
    }

    public int getIfIncrementer() {
        return ifIncrementer;
    }
    public int incrementIfIncrementer() {
        ifIncrementer++;
        return ifIncrementer;
    }
    
    public int getWhileIncrementer() {
        return whileIncrementer;
    }
    public int incrementWhileIncrementer() {
        whileIncrementer++;
        return whileIncrementer;
    }

    public int getStacksizeNeededMain() {
        return stacksizeNeededMain;
    }

    public int measureStacksizeNeededMain(DecacCompiler compiler) {
        stacksizeNeededMain =  gbCounter + compiler.getRegisterManagement().getMaxTempVariables();
        return stacksizeNeededMain;
    }


}
