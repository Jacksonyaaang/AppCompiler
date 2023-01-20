

package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl15
 * @date 01/01/2023
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    EnvironmentExp parentEnvironment;
    private Map<Symbol, ExpDefinition> envExp;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.envExp = new HashMap<Symbol, ExpDefinition>();
    }

    public static class DoubleDefException extends Exception {
        public DoubleDefException (String message){
            super(message);
        }
        private static final long serialVersionUID = -2733379901827316441L;
    }

    private EnvironmentExp currentExp = null;  // used to get the current environement expression when "get(symbol)"

    public EnvironmentExp getCurrentExp() {
        return currentExp;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        if (!envExp.containsKey(key)&&parentEnvironment==null)
            return null;
        if (envExp.containsKey(key)){
            currentExp=this;
            return envExp.get(key);
        }
        return parentEnvironment.get(key);
    }

    /**
     * Cette methode retourne la premier methode qu'elle rencontre 
     * qui a un index égal à celui donnée en paramétre 
     */
    public MethodDefinition getMethodIndex(int index) {
        for(Map.Entry<Symbol, ExpDefinition> entry : envExp.entrySet()){
            if (entry.getValue() instanceof MethodDefinition){
                if ( ((MethodDefinition) entry.getValue()).getIndex() == index){
                    return (MethodDefinition) entry.getValue();
                }
            }
        }
        if (parentEnvironment == null){
            return null;
        }
        return parentEnvironment.getMethodIndex(index);
    }

    /**
     * Retourne l'environement qui contient la methode donnée en paramétre  
     */
    public EnvironmentExp getMethodEnvioremnent(MethodDefinition methodDefinition) {
        for(Map.Entry<Symbol, ExpDefinition> entry : envExp.entrySet()){
            if (entry.getValue() ==  methodDefinition){
                return this;
            }
        }
        if (parentEnvironment == null){
            return null;
        }
        return parentEnvironment.getMethodEnvioremnent(methodDefinition);
    }


    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (!envExp.containsKey(name)) envExp.put(name, def);
        else {throw new DoubleDefException("La variable est déjà déclarée");}
        //throw new UnsupportedOperationException("not yet implemented");
    }

    public Map<Symbol, ExpDefinition> getExp(){
        return envExp;
    }

    public void setParentEnvironment(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }
    public EnvironmentExp getParent(){
        return parentEnvironment;
    }


}
