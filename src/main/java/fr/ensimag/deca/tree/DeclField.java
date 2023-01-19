package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManagementUnit;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.*;
import static org.mockito.Mockito.mockingDetails;
import org.apache.log4j.Logger;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class DeclField extends AbstractDeclField {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    final private Visibility visibility;
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    public Visibility getVisibility() { return visibility; }

    public AbstractIdentifier getType() { return type; }

    public AbstractIdentifier getVarName() { return varName; }

    public AbstractInitialization getInitialization() { return initialization; }

    @Override
    public void decompile(IndentPrintStream s) {
        //A FAIRE DECOMPILE PROTECTED
        if(getVisibility()==Visibility.PROTECTED){
            s.print("protected ");
        }
        else {
            s.print("public ");
        }
        getType().decompile(s);
        s.print(" ");
        getVarName().decompile(s);
        getInitialization().decompile(s);
        s.print(";");

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // A FAIRE, INTEGRATE VISIBILITY INTO THE OUTPUT
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override 
    String prettyPrintNode() {
        String visibiltyOutput = visibility == Visibility.PROTECTED ? " [visibility=PROTECTED] " : " [visibility=PUBLIC] ";
        return visibiltyOutput + " DeclField" ;//+  super.prettyPrintNode();
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        LOG.debug("[DeclField][verifyDecleField] Verify a Field declaration");
        ClassDefinition tmpClass;
        for (tmpClass = currentClass.getSuperClass(); tmpClass != null; tmpClass = tmpClass.getSuperClass()){
            if (tmpClass.getMembers().getExp().containsKey(varName.getName()) && 
                varName.getDefinition() instanceof MethodDefinition){
                    throw new ContextualError(" Il existe une  methode qui posséde le même nom que le field =  "+ varName.getName().getName(), getLocation());
                }
        }
        Type t = type.verifyType(compiler);
        type.setType(t);
        //Vérification de la condition type =/= void de la règle 3.17
        if(t.isVoid()) {
            throw new ContextualError("Déclaraion de Field de type void impossible", getLocation());
        }
        //initialization.verifyInitialization(compiler, type.getType(), localEnv, currentClass);
        int _index = currentClass.getNumberOfFields() + 1;
        FieldDefinition fieldDf = new FieldDefinition(type.getType(), getLocation(), visibility, currentClass, _index);      //new FieldDefinition(type.getType(), varName.getLocation());
        varName.setDefinition(fieldDf);
        try{
            localEnv.declare(varName.getName(), varName.getExpDefinition()); 
        } 
        catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("y'a deja un field ou une methode du même nom", getLocation());
        }
        currentClass.setNumberOfFields(_index);    
    }

    @Override
    protected void verifyinitFieldPass3(DecacCompiler compiler, EnvironmentExp localEnv, 
        ClassDefinition currentClass) throws ContextualError{
        initialization.verifyInitialization(compiler, type.getType(), localEnv, currentClass);
    }

    @Override
    public void CodeGenPlaceZeroInField(DecacCompiler compiler) throws CodeGenError {
        if (type.getType() == compiler.environmentType.FLOAT){
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.getR(0)),
            "loading 0.0  into memory to initialize field with type float to 0");
        }
        else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.getR(0)),
                "loading 0  into memory to initialize field to 0");
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(1)),
        "loading class (this) into memory when working with field "+getVarName().getName());
        compiler.addInstruction(new STORE(Register.getR(0),new RegisterOffset(((FieldDefinition) (varName.getExpDefinition())).getIndex(), Register.getR(1))),
                    "Saving field  "+getVarName().getName()+ " into memory");
    }
    
    @Override
    public void codeGenDelField(DecacCompiler compiler) throws CodeGenError {
        //If the field has been initialized, then we must place the value that has been 
        //assigned to field, and place it in the memory location that holds the field
        if (initialization instanceof Initialization ){ 
            GPRegister assignRegister = this.LoadAndReserveARegister(compiler);
            this.initialization.codegenInitial(compiler);
            compiler.addInstruction(new STORE(this.initialization.getRegistreDeRetour(),
                                        new RegisterOffset(this.varName.getFieldDefinition().getIndex(), assignRegister)), "Initializing the field "+ getVarName().getName()
                                                                +" and loading it into memory"); 
        }
    }

    /**
     * Cette méthodes est utilisé  
     */
    protected GPRegister LoadAndReserveARegister(DecacCompiler compiler) throws CodeGenError {
        GPRegister regReserved = null;
        if (compiler.getRegisterManagement().areThereAnAvaliableRegsiterSup2()){
            regReserved = compiler.getRegisterManagement().getAnEmptyStableRegisterAndReserveIt(); 
            assert(regReserved !=null );
            LOG.debug("[DeclField][LoadAndReserveARegister]  Reserving an non empty register with the name " + regReserved);
        }
        else{
            throw new CodeGenError(getLocation(),"[DeclField][LoadAndReserveARegister] We must have empty registers when begin the field declaration process");
        }

        return regReserved;
    }





}

