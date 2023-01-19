package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Selection extends AbstractLValue {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);


    protected AbstractExpr obj;
    protected AbstractIdentifier field;
    //protected AbstractDeclField field;

    public Selection(AbstractExpr obj, AbstractIdentifier field){
        Validate.notNull(obj);
        Validate.notNull(field);
        this.obj = obj;
        this.field = field;
    }

    public AbstractIdentifier getField() {return field;}
    public AbstractExpr getObj() {return obj;}

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError {
        obj.codeGenInst(compiler);
        if (!compiler.getCompilerOptions().isNoCheck()){
            compiler.addInstruction(new CMP(new NullOperand(), obj.getRegisterDeRetour()), null);
            compiler.addInstruction(new BEQ(new Label("deref_null_error")), 
                                    "Checking if the class identifier is null");
            compiler.getErrorManagementUnit().activeError("deref_null_error");
        }
        compiler.addInstruction(new LOAD(
                        new RegisterOffset( ((Identifier)field).getFieldDefinition().getIndex(), obj.getRegisterDeRetour()), obj.getRegisterDeRetour()),
                         "Loading the field " + field.getName() +" into a register "); 
        this.setRegisterDeRetour(obj.getRegisterDeRetour());
        this.transferPopRegisters(obj.getRegisterToPop());
    }
    
    /**
     * the return type is obvously a "class"
     *    expression found : this, Lvalue (for class),  Cast
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)  throws ContextualError {
            Type t = obj.verifyExpr(compiler, localEnv, currentClass);
            this.obj.setType(t);
            //setType(t);  //error, miss 'this.type'
            if (!t.isClass()){
                throw new ContextualError("[Using Error] The first selection must be a class or this ", getLocation());
            }
            //if (currentClass.getMembers().get(((Identifier)this.field).getName())==null){
            if (((ClassType)t).getDefinition().getMembers().get(((Identifier)this.field).getName())==null){
                //we should verify the de definition exists from the first expression class insatead of the current class
                throw new ContextualError("[Using Error] Can't find the field in the prevous declarations ", getLocation());
                
            }else{
                FieldDefinition envField =(FieldDefinition) currentClass.getMembers().get(((Identifier)this.field).getName());
                Visibility visiField = envField.asFieldDefinition("it's not a field type", getLocation()).getVisibility();
                field.setDefinition(envField);
                if (visiField==Visibility.PROTECTED){
                    //if (((Identifier)this.field).getFieldDefinition().getVisibility()==Visibility.PROTECTED){
                    LOG.info("i'm entering the  protected field !!!");
                    ClassDefinition classDes = ((FieldDefinition)this.field.getDefinition()).getContainingClass();

                    if (!currentClass.getType().isSubClassOf(classDes.getType())||!((ClassType)t).isSubClassOf(classDes.getType())) {
                        throw new ContextualError("[Using Error] Obey the second condition, the current class must be the sub class of the field class", getLocation());
                    } 
                    
                }
            }

           return currentClass.getMembers().get(((Identifier)this.field).getName()).getType();
        }
        //this.obj1.obj2.untrucprimitif=8;
    //     Type typeReturn = null;
    // if (this.obj instanceof This){
    //     Map<Symbol, ExpDefinition> envCurrent = currentClass.getMembers().getExp();
    //     if (!envCurrent.containsKey(((Identifier)this.field).getName())){
    //         throw new ContextualError("[Using Error] the content after 'this' for selection must be previously declared", getLocation());
    //     }else{
    //         //verify the field 
    //         typeReturn=helperPublic(currentClass.getMembers(), this.field);
    //     }   
    // }else if (this.obj instanceof AbstractLValue&&this.obj.getType().isClass()){
    //     //for the class 
    //     EnvironmentExp local = currentClass.getMembers();
    //    // if (local.get(((Identifier)this.obj).getName()) == null){ 
    //     if (compiler.environmentType.getEnvTypes().containsKey(((Identifier)this.obj).getName())){ 
    //          //not found the declaration of class even though in the super-classs
    //          throw new ContextualError("[Using Error] The class that you entered never has been declared before", getLocation());
    //     }
    //     // 2 cases , one for public, one for protected 
    //     if (((Identifier)this.field).getFieldDefinition().getVisibility()==Visibility.PUBLIC){
    //         //get that field's env , useful in the helper public 
    //         EnvironmentExp distanceEnvExp = local.getCurrentExp();  //cuz we already set it in last "if"
    //         typeReturn = helperPublic(distanceEnvExp, field);
    //     }
    //     if (((Identifier)this.field).getFieldDefinition().getVisibility()==Visibility.PROTECTED){
    //         EnvironmentExp distanceEnvExp = local.getCurrentExp();
    //         ClassDefinition classDestine = this.field.getClassDefinition();
    //         typeReturn = helperProtected(localEnv, distanceEnvExp, currentClass, classDestine, field);
    //     }
        
    // }else if (this.obj instanceof Cast){


    // }else {
    //     throw new ContextualError("[Using Error] the variable type in a selection must be a class or keyword 'this'", getLocation());
    // }
    
    // //obj1.obj2.obj3.field3="";  this.th
    
    // return typeReturn;

    /*
     * when the visibility of the field is public  
     */
//     private Type helperPublic(EnvironmentExp envClass2, AbstractIdentifier fld) throws ContextualError{
//         Type typeReturn = null;
//         if (((Identifier)fld).getType().isClass()){
//             //verify this field can be found in the current (or super-classes) env-expression
//             if (envClass2.get(((Identifier)fld).getName())==null){
//                 throw new ContextualError("[Using Error] the field (class) haven't been declared before", getLocation());
//             }
//             typeReturn = verifyExpUlterieure(null, envClass2, null);   //the parameters are not charged yet !!!!!!!!!!!!!!!!!

//         }else{ 
//             //if it's not a class, then should be found in the current (its) env exp (not in the super-classes)
//             if (!envClass2.getExp().containsKey(((Identifier)fld).getName())){
//                 throw new ContextualError("[Using Error] the field haven't been declared before", getLocation());
//             }
//             //the others elm (except class) in the current field
//             typeReturn = ((Identifier)fld).getType();
//         }
//         return typeReturn;
//     }
//     /*
//      * when the visibility of the field is protected  
//      */
//     private Type helperProtected(EnvironmentExp envCurrent, EnvironmentExp envClass2,
//                                 ClassDefinition currClassDefinition, ClassDefinition desClassDefinition ,
//                                 AbstractIdentifier fld) throws ContextualError{
//             Type typeReturn=null;
            
//             //verify the condition 1;
//             ClassType currClass = currClassDefinition.getType();
//             ClassType desClass = desClassDefinition.getType();
//             if (!currClass.isSubClassOf(desClass)){
//                 throw new ContextualError("[Using Error] the destine field is protected, should use a subclass to have an access ", getLocation());
//             }
//             if (((Identifier)fld).getType().isClass()){
//                 if (envClass2.get(((Identifier)fld).getName())==null){
//                     //can't find the name of the class on the empilement 
//                     throw new ContextualError("[Using Error] the field (class) haven't been declared before", getLocation());
//                 }
//                 typeReturn = verifyExpUlterieure(null, envClass2, desClassDefinition)
//             }else{
//                 if (!envClass2.getExp().containsKey(((Identifier)fld).getName())){
//                     throw new ContextualError("[Using Error] the field haven't been declared before", getLocation());
//                 }
//                 typeReturn = ((Identifier)fld).getType();
//             }
            
                                    
//             return typeReturn;
//     }

// }




    // /**
    //  * use this method to complete the first verifyExp, reason : solve the 'this isn't on the first position' problem 
    //  * @param compiler
    //  * @param localEnv
    //  * @param currentClass
    //  * @return
    //  * @throws ContextualError
    //  */
    // private Type verifyExpUlterieure(DecacCompiler compiler, EnvironmentExp localEnv, 
    //                                 ClassDefinition currentClass) 
    //                                 throws ContextualError{
        

    //             this.field=
    //             if (this.obj instanceof AbstractLValue){
    //                 //for the class
    //                 if (currentClass.getMembers().get(((Identifier)this.obj).getName()) == null){ 
    //                     //not found the declaration of class even though in the super-classs
    //                     throw new ContextualError("[Using Error] The class that you entered never has been declared before", getLocation());
    //                 }
    //                 //pass
                    
                    
    //             }else if (this.obj instanceof Cast){


    //             }else {
    //                 throw new ContextualError("[Using Error] the variable type in a selection must be a class or keyword 'this'", getLocation());
    //             }
    //             //now the field :
    //             Type typeField = this.field.getType();
                
    //             //obj1.obj2.obj3.field3="";
                
    //             return null;
    // }
    // //method not finished 

/* 
        // TODO Auto-generated method stub
        return compiler.environmentType.INT;
    } */

    @Override
    public void decompile(IndentPrintStream s) {
        getObj().decompile(s);
        s.print(".");
        getField().decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);       
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);           
        field.iter(f) ;
    }
    
}
