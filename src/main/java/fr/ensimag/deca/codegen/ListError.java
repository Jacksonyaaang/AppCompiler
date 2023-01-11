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
    private Map<String, Boolean> list = new HashMap<String, Boolean>();
    public ListError(){
        this.list.put("stack_overflow_error", false);
        this.list.put("overflow_error", false);
        this.list.put("io_error", false);
    }

    public void Active_Stack_Overflow_Error(String err_name){
        if(!this.list.containsKey(err_name)){
            throw new IllegalArgumentException("le nom "+ err_name +" ne correspond à aucune erreur existante\n");
        }
        else{
            this.list.replace(err_name, true);
        }
    }
    
    // public void Active_Stack_Overflow_Error(){
    //     this.list.replace("stack_overflow_error", true);
    // }
    
    // public void Active_Overflow_Error(){
    //     this.list.replace("overflow_error", true);
    // }

    // public void Active_Io_Error(){
    //     this.list.replace("io_error", true);    
    // }

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
