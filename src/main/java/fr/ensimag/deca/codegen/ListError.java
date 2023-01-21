package fr.ensimag.deca.codegen;
import java.util.Map;
import java.util.HashMap;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import org.apache.log4j.Logger;


public class ListError {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

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
        this.list.put("heap_overflow_error", false);
        this.list.put("cast_error", false);
        /*
         * 
         */
        this.list.put("int_allocation_table_must_be_positive", false);
        this.list.put("table_dimension_are_not_respected", false);
    }

    public void activeError(String err_name){
        if(!this.list.containsKey(err_name)){
            throw new IllegalArgumentException("le nom "+ err_name +" ne correspond à aucune erreur existante\n");
        }
        else{
            LOG.debug("ERROR is being made to value true " + err_name );
            this.list.replace(err_name, true);
        }
    }

    /**
     * writeListError écrit en assembleur tout les erreurs activées
     * @param compiler
     */
    public void writeListError(DecacCompiler compiler){
        for(Map.Entry<String, Boolean> set : list.entrySet()){
            if(set.getValue()){
                compiler.addLabel(new Label(set.getKey()));
                compiler.addInstruction(new WSTR("Error: " +set.getKey()));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
            }
        }
    }
    

    public Map<String, Boolean> getList(){
        return this.list;
    }
}
