
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
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

import java.util.HashMap;
import java.util.Map;


public class RegisterMangementUnit {
    
    public int numberOfRegister;
    public Map<GPRegister, Boolean> registerOccupation =  new HashMap<GPRegister, Boolean>();
    public GPRegister emptyRegsiter;

    /**
     * 
     * @param numberOfRegister le nombre de registre qu'on peut utiliser, par défaut égal à 16 mais 
     * varie entre 4 et 16
     */
    public RegisterMangementUnit(int numberOfRegister){
        assert(numberOfRegister <= 16 && numberOfRegister >=4 );
        for (int reg = 0; reg < numberOfRegister; reg++ ){
            registerOccupation.put(Register.getR(reg), false);
        }
    }

    

    /**
     * Verifie que le registre chosi n'est pas un registre égal au registre R0 
     * ou le registre R1
     * @return vrai si le registre n'est pas égal à R1 ou R2
     */
    private Boolean verifyRegisterIsStable(GPRegister registre){ 
        if (registre == Register.getR(0) 
            || registre == Register.getR(1)){
                return false;
        }
        return true;
    }

    /**
     * Indique si une registre est disponible qui est different de R0 et R1
     * @return vrai, si un registre est disponible et faux sinon 
     */
    public boolean isThereAnAvaliableRegsiterSup2(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            GPRegister registre = entry.getKey();
            if(value == false){
                if (verifyRegisterIsStable(registre)){
                    return true;
                }
            }
        }
        return false;
    }


    public boolean areThereAnyAvalaibleRegister(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            if (value == false){return true;}
        }
        return false;
    }

    /**
     * Reserve un registre et le retourne, le registre doit être different R0/R1
     * @return  un registre different de r0 et r1
     */
    public GPRegister getAnEmptyStableRegisterAndReserveIt(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            GPRegister registre = entry.getKey();
            if(value == false){
                if (verifyRegisterIsStable(registre)){
                    registerOccupation.replace(registre, true);
                    return entry.getKey();
                }
            }
        }
        return null;
    }

        /**
     * Reserve un registre et le retourne, le registre doit être different R0/R1
     * @return  un registre different de r0 et r1
     */
    public GPRegister getAUsedStableRegisterAndKeepItReserved(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            GPRegister registre = entry.getKey();
            if(value == true){
                if (verifyRegisterIsStable(registre)){
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Cette fonction est utilisée dans le cas ou aucun registre est disponible 
     * @return n'importe quelle registre vide
     */
    public GPRegister getAnyEmptyRegisterAndReserveIt(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            if (value == false){
                registerOccupation.put(entry.getKey(), true);
                return entry.getKey();
            }
        }
        return null;
    }
    
    public GPRegister freeAllRegisters(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            registerOccupation.replace(entry.getKey(), false);
        }
        return null;
    }

    public void freeRegister(GPRegister register){
        registerOccupation.replace(register, false);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("-----Start table----\n");
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            String status = entry.getValue() ? "Taken": "empty";
            output.append(entry.getKey() + "->" + status +" \n");
        }
        output.append("-----End table----");
        return output.toString();        
    }    
}


// // x = (a*(a*(a*(
// //     5*4)  a*(a*(a*(a*(b))))))));  (LOAD #5, R2, store 2(GB), R2)

// // // push r2
// // // R2 5
// // // push r3
// // // R3 4 
// // // R3 

// int x = (a*(a*(a*(a*(b)))))*a;


// // // R2/R3/R4


// // // PUSH R2/


// // // POP R2 

// // // a* b 

// // // x = 5*a

// // // MUL 2(GB), R2

// // // MUL getoperand-(registre ou une adresse),         MUL (), R2

// //     public boolean isThereAnyAvaliableRegister(){
        
// //     }

    
// }
