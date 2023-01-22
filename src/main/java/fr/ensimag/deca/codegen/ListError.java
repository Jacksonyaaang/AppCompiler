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
    //heap_overflow_error: déclenchée quand on essaye de reserver plus d'espace dans le tas 
    //cast_error : déclenchée quand on essaye de faire un cast illégal
    //int_allocaterveion_table_must_be_strictly_positive : déclenchée quand l'utilisateur 
    // essaye d'allourer de l'espace d'un tableau avec un indice négatif
    //int_selection_table_must_be_positive : déclenchée quand l'utilisateur 
    // essaye d'acceder un element du tableau avec des indice négatif
    //table_dimension_are_not_respected : déclenchée quand l'utilisateur 
    // essaye d'acceder un tableau avec un indice plus large que la dimension du tableau
    //table_dimension_can_not_be_changed : déclenchée quand l'utilisateur 
    // essaye de changer la dimension d'un tableau en faisant par exemple x.size1D = 5;
   
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
         * Erreur Extension 
         */
        this.list.put("int_allocation_table_must_be_strictly_positive", false);
        this.list.put("int_selection_table_must_be_positive", false);
        this.list.put("table_dimension_are_not_respected", false);
        this.list.put("table_dimension_can_not_be_changed", false);
        
    }

    public void activeError(String err_name){
        if(!this.list.containsKey(err_name)){
            throw new IllegalArgumentException("le nom "+ err_name +" ne correspond à aucune erreur existante\n");
        }
        else{
            LOG.debug("L'erreur " + err_name + " est activée " );
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
