
/**
 * .
 * Cette classe fait la gestion des registres et stocke l'occupation 
 * acutuelle des registers et et calcule le nombre de variable temporaires maximal
 * suivant l'usage des registre
 *
 * @author gl15
 * @date 01/01/2023
 */

package fr.ensimag.deca.codegen;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class RegisterManagementUnit {
    
    public int numberOfRegister;
    /**
     * @registerOccupation indique si le registre est utilisée
     * @registerUsage  indique le nombre d'utilisation de chaque registre
     */
    public Map<GPRegister, Boolean> registerOccupation =  new HashMap<GPRegister, Boolean>();
    public Map<GPRegister, Integer> registerUsage =  new HashMap<GPRegister, Integer>();
    public HashSet<GPRegister> usedRegistersInProgram =  new HashSet<GPRegister>();
    /**
     * @maxTempVariables
     * maxTempVariables calcule le nombre de variable
     * temporaire maximal qu'on peut utilise
     * Cette valeur sera utilisée pour calculer la taille de la pile maximal
     */
    public int maxTempVariables = 0;
    
        
    public int getMaxTempVariables() {
        return maxTempVariables;
    }

    public void setMaxTempVariables(int maxTempVariables) {
        this.maxTempVariables = maxTempVariables;
    }


    /**
     * @tempVariables
     * tempVariables  calcul le nombre variable temporaire actuel
     *  dans une instruction donnée
     */
    public int tempVariables = 0;

    /**
     * increaseTempVariables augemente tempVariables par 
     * spaceNeeded, cette méthode sera appelée lors de la manipualtion des methodes
     * ou on ajoutera le nombre d'empilenement necessaire
     * @param spaceNeeded
     */
    public void increaseTempVariables(int spaceNeeded){
        tempVariables = tempVariables + spaceNeeded;
        if (tempVariables >= maxTempVariables){
            maxTempVariables = tempVariables;
        }
    }


    /**
     * decreaseTempVariables dimunie tempVariables par 
     * spaceNeeded, cette méthode sera appelée quand on quitte on méthode
     * pour libere l'espace necessaire
     * @param spaceNeeded
     */
    public void decreaseTempVariables(int spaceNoLongerNeeded){
        tempVariables = tempVariables - spaceNoLongerNeeded;
    }

    /**
     * RegisterManagementUnit initalize les deux structures de données qu'on utilisera pour la manipulation 
     * des registres
     * @param numberOfRegister le nombre de registre qu'on peut utiliser, par défaut égal à 16 mais 
     * varie entre 4 et 16
     */
    public RegisterManagementUnit(int numberOfRegister){
        assert(numberOfRegister <= 16 && numberOfRegister >=4 );
        for (int reg = 0; reg < numberOfRegister; reg++ ){
            registerOccupation.put(Register.getR(reg), false);
            registerUsage.put(Register.getR(reg), 0);
        }
    }

    /**
     * verifyRegisterIsStable Verifie que le registre chosi n'est pas un registre égal au registre R0 
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
     * areThereAnAvaliableRegsiterSup2 Indique si une registre est disponible qui est different de R0 et R1
     * @return vrai, si un registre est disponible et faux sinon 
     */
    public boolean areThereAnAvaliableRegsiterSup2(){
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

    /**
     * areThereAnyAvalaibleRegister indique s'il y a un registre vide dans toute la liste des 
     * variables
     * @return 
     */
    public boolean areThereAnyAvalaibleRegister(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            if (value == false){return true;}
        }
        return false;
    }

    /**
     * getAnEmptyStableRegisterAndReserveIt Reserve un registre et le retourne,
     * le registre doit être different R0/R1
     * @return  un registre different de r0 et r1
     */
    public GPRegister getAnEmptyStableRegisterAndReserveIt(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            Boolean value = entry.getValue();
            GPRegister registre = entry.getKey();
            if(value == false){
                if (verifyRegisterIsStable(registre)){
                    usedRegistersInProgram.add(registre);
                    registerOccupation.replace(registre, true);
                    registerUsage.replace(registre, 1);
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * getAUsedStableRegisterAndKeepItReserved Reserve un registre utilisée et le retourne,
     *  le registre doit être different R0/R1
     * @return  un registre déja utilisée different de r0 et r1
     */
    public GPRegister getAUsedStableRegisterAndKeepItReserved(){
        GPRegister returnedRegister = null;
        //We initialize min search with this
        int usage = -1;
        tempVariables++;
        if (tempVariables >= maxTempVariables){
            maxTempVariables = tempVariables;
        }
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
     * getAnyEmptyRegisterAndReserveIt est utilisée dans le cas ou aucun registre est disponible 
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
    
    /**
     * freeAllRegisters est utilisée à la fin d'une instruction, 
     * afin de liberer tout les registre au point de vue de la classe
     */
    public void freeAllRegisters(){
        for (Map.Entry<GPRegister, Boolean> entry : registerOccupation.entrySet()) {
            tempVariables = 0;
            registerOccupation.replace(entry.getKey(), false);
            registerUsage.replace(entry.getKey(),0);
        }
    }

    /**
     * freeRegister libere un registre, donc le rend 
     * non utilisée et rend son usage égal à 0
    */
    public void freeRegister(GPRegister register){
        registerOccupation.replace(register, false);
        registerUsage.replace(register,0);
    }
    
    /**
     * Donne le nombre de registre utilisée dans une méthode qui sont 
     * different des registres non stable R0/R1
    */
    public int numberOfRegisterUsedInMethod(){
        return usedRegistersInProgram.size();   
    }


    /**
     * decrementOccupationRegister décremente l'usage d'un registre
     * et elle appellée quand on terminé l'exploitation du registre dans un context 
     * spécifique
    */
    public void decrementOccupationRegister(GPRegister register){
        if (!(registerUsage.get(register) == 0)){
            /* Chaque fois qu'on libere un registre utilisée plus qu'une fois, 
                on on décrement le nombre de variable temporaires necessaires.*/
            if (!(registerUsage.get(register) == 1)){
                tempVariables--;
            }
            registerUsage.replace(register, registerUsage.get(register)-1);
            if (registerUsage.get(register) == 0){
                registerOccupation.replace(register, false);
            }
        }
    }
    
    public void pushUsedRegistersMethod(DecacCompiler compiler){
        for (GPRegister register :  usedRegistersInProgram){
            compiler.getProgram().addFirst(new PUSH(register), "Pushing and poping registers used in the method");
        }
    }

    public void popUsedRegistersMethod(DecacCompiler compiler){
        for (GPRegister register :  usedRegistersInProgram){
            compiler.addInstruction(new POP(register), "Pushing and poping registers used in the method");
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

