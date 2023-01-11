
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
    public Map<GPRegister, Integer> registerUsage =  new HashMap<GPRegister, Integer>();
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
            registerUsage.put(Register.getR(reg), 0);
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
                    registerUsage.replace(registre, 1);
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
        GPRegister returnedRegister = null;
        int usage = -1;
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            GPRegister registre = entry.getKey();
            if(value == true){
                if (verifyRegisterIsStable(registre)){
                    if (usage == -1){
                        usage = registerUsage.get(registre);
                        returnedRegister = registre;
                    }
                    else if (registerUsage.get(registre) < usage) {
                        usage = registerUsage.get(registre);
                        returnedRegister = registre;
                    }
                }
            }
        }
        registerUsage.replace(returnedRegister, registerUsage.get(returnedRegister)+1);
        return returnedRegister;
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
                registerUsage.replace(entry.getKey(), registerUsage.get(entry.getKey())+1);
                return entry.getKey();
            }
        }
        return null;
    }
    
    public GPRegister freeAllRegisters(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            registerOccupation.replace(entry.getKey(), false);
            registerUsage.replace(entry.getKey(),0);
        }
        return null;
    }

    public void freeRegister(GPRegister register){
        registerOccupation.replace(register, false);
        registerUsage.replace(register,0);
    }

    public void decrementOccupationRegister(GPRegister register){
        if (!(registerUsage.get(register) == 0)){
            registerUsage.replace(register, registerUsage.get(register)-1);
            if (registerUsage.get(register) == 0){
                registerOccupation.replace(register, false);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("-----Start table----\n");
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            String status = entry.getValue() ? "Taken": "empty";
            output.append(entry.getKey() + "->" + status + " ; Usage = "+ registerUsage.get(entry.getKey()) +" \n");
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
