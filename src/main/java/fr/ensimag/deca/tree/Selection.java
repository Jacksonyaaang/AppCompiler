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
import fr.ensimag.deca.context.TableType;
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

    private static final Logger LOG = Logger.getLogger(Selection.class);

    protected AbstractExpr obj;
    protected AbstractIdentifier field;
    /*
     * Cette element sera utilisé pour accéder au caratéristique d'un tableau    
     * qui sont size1D et size2D 
     */

    protected int tableIndex = 0;


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
        compiler.addComment("--------BeginSelection--------"+getLocation()+"-----");
        obj.codeGenInst(compiler);
        if (!compiler.getCompilerOptions().isNoCheck()){
            compiler.addInstruction(new CMP(new NullOperand(), obj.getRegisterDeRetour()), null);
            compiler.addInstruction(new BEQ(new Label("deref_null_error")),
                                    "Checking if the class identifier is null");
            compiler.getErrorManagementUnit().activeError("deref_null_error");
        }
        if (obj.getType().isTable()){
            compiler.addInstruction(new LOAD(
                new RegisterOffset( tableIndex, obj.getRegisterDeRetour()), obj.getRegisterDeRetour()),
                 "Loading the field " + field.getName() +" into a register "); 
        }
        else {
            compiler.addInstruction(new LOAD(
                        new RegisterOffset( ((Identifier)field).getFieldDefinition().getIndex(), obj.getRegisterDeRetour()), obj.getRegisterDeRetour()),
                         "Loading the field " + field.getName() +" into a register "); 
        }
        this.setRegisterDeRetour(obj.getRegisterDeRetour());
        this.transferPopRegisters(obj.getRegisterToPop());
        compiler.addComment("--------BeginSelection--------"+getLocation()+"-----");
    }

    /**
     * the return type is obvously a "class"
     *    expression found : this, Lvalue (for class),  Cast
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
            FieldDefinition fieldDefi;
            Type type = obj.verifyExpr(compiler, localEnv, currentClass);
            this.obj.setType(type);
            if (type.isTable()){
                traitementAcessDimension(compiler, localEnv, currentClass);
                setType(compiler.environmentType.INT);
                return compiler.environmentType.INT;
            }     
            //setType(t);  //error, miss 'this.type'
            if (!type.isClass()){
                throw new ContextualError(" Le type de l'expression utilisé pour une sélection doit être un type de classe ou \"this\"", getLocation());
            }
            LOG.debug("[Selection][verifyExpr] Class type is  " + ((ClassType)((obj).getType())).getDefinition().getType().getName());
            fieldDefi = (FieldDefinition)(((ClassType)((obj).getType())).getDefinition().getMembers().get(((Identifier)this.field).getName()));
            field.setDefinition(fieldDefi);
            if (fieldDefi==null){
                throw new ContextualError("Le champs " + field.getName().getName() + " n'est pas défini", getLocation());


            }else{
                if (((Identifier)this.field).getFieldDefinition().getVisibility()==Visibility.PROTECTED){
                    ClassDefinition classDes = ((FieldDefinition)this.field.getDefinition()).getContainingClass();
                     if (!currentClass.getType().isSubClassOf(classDes.getType())||!((ClassType) type).isSubClassOf(classDes.getType())) {

                        throw new ContextualError(" La classe "+ currentClass.getType().getName().getName()+" doit être une sous-classe de " + currentClass.getSuperClass().getType().getName().getName() + "où est défini le champs" + getField().getName().getName(), getLocation());
                    }
                    
                }
            }
            //return compiler.environmentType.INT;
            setType(fieldDefi.getType());
            return getType();
        }

    public void traitementAcessDimension(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        if (field.getName() == compiler.createSymbol("size1D") || field.getName() == compiler.createSymbol("size2D")){
            field.setType(compiler.environmentType.INT);
            field.setDefinition(new FieldDefinition( compiler.environmentType.INT, Location.BUILTIN, Visibility.PUBLIC, 0));
            if (field.getName() == compiler.createSymbol("size2D")){
                field.setDefinition(new FieldDefinition( compiler.environmentType.INT, Location.BUILTIN, Visibility.PUBLIC, 2));
                if (((TableType)(obj.getType())).getDimension() == 1){
                    throw new ContextualError("Le tableau est 1D", getLocation());
                }
                tableIndex = 1;
            }   
        }
        else{
            throw new ContextualError("Pour accéder au caratéristique du tableau, il utiliser les identificateurs suivants mettre size1D, size2D", getLocation());
        }

    }



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
