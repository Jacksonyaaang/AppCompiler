package fr.ensimag.deca.codegen;
import java.util.Map;
import java.util.HashMap;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.ERROR;

public class ListError {
    //list[0]:statut de stack_overflow_error
    //list[1]:statut de overflow_error
    //list[2]:statut de io_error
    //list[3]:statut de div0_error
    //list[4]:statut de missing_return_error
    //list[5]:statut de deref_null_error
    
    //io_error: Input/Output error: déclenchée quand un argument de méthode ou une 
    //variable stockant l'objet retourné n'a pas le bon type.
    //div0_error: déclenchée par une tentative de division par zéro.
    //missing_return_error: déclenchée par l'absence de return dans une méthode non void.
    //dereferencing_null_error: déclenchée par une tentative de déréférencement d'un objet null,
    // i.e. d'utiliser un objet non instancié ou attribut d'objet non initialisé.
    private Map<String, Boolean> list = new HashMap<String, Boolean>();
    public ListError(){
        this.list.put("stack_overflow_error", false);
        this.list.put("overflow_error", false);
        this.list.put("io_error", false);
        this.list.put("div0_error", false);
        this.list.put("missing_return_error", false);
        this.list.put("deref_null_error", false);

    }

    public void Active_Stack_Overflow_Error(String err_name){
        if(!this.list.containsKey(err_name)){
            throw new IllegalArgumentException("le nom "+ err_name +" ne correspond à aucune erreur existante\n");
        }
        else{
            this.list.replace(err_name, true);
        }
    }


    public void WriteListError(DecacCompiler compiler){
        for(Map.Entry<String, Boolean> set : list.entrySet()){
            if(set.getValue()){
                compiler.addInstruction(new WSTR(set.getKey()));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
            }
        }
    }
    

    public Map<String, Boolean> getList(){
        return this.list;
    }
}
