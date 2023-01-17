package fr.ensimag.deca.codegen;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities used for IMA code generation.
 *
 * TableDeMethode sera utilisée pour stockée l'etat de la table de méthode et elle 
 * sera aussi utilisée lors de la manipulation des classes à fin de trouver la postion 
 * des classe
 *
 * @author Mehdi
 * @date 01/01/2023
 */

public class TableDeMethode {
    
    /**
     * @adresseTableDeMethod indique la position de la table de méthodes pour 
     * chaque class
     */
    private Map<ClassDefinition, RegisterOffset> adresseTableDeMethod  =  new HashMap<ClassDefinition, RegisterOffset>();

    public Map<ClassDefinition, RegisterOffset> getAdresseTableDeMethod() {
        return adresseTableDeMethod;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("-----Start table Method Table----\n");
        for (Map.Entry<ClassDefinition, RegisterOffset> entry : adresseTableDeMethod.entrySet()) {
            output.append(entry.getKey().toString() + " -> " +  entry.getValue());
        }
        output.append("-----End table Method Table  ----\n");
        return output.toString();        
    }    
}


