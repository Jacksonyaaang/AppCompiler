package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.codegen.RegisterManagementUnit;
import fr.ensimag.deca.codegen.StackManagementUnit;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl15
 * @date 01/01/2023
 */
public class DeclClass extends AbstractDeclClass {
    private static final Logger LOG = Logger.getLogger(DeclClass.class);


    protected final AbstractIdentifier name ;
    protected final AbstractIdentifier superClass; 
    protected final ListDeclField fields;
    protected final ListDeclMethod methods;

    protected MethodDefinition defintionMethodInitClass;
    protected  IMAProgram classProgram = new IMAProgram();
    
    /**
     * Ces elements sont utilisées pour géneré le bloc 
     * de la méthode init
     */
    RegisterManagementUnit initMethodRegisterManagementUnit = null; 
    StackManagementUnit initMethodStackManagementUnit = new StackManagementUnit();
    IMAProgram initMethodProgram = new IMAProgram();


    public void codeGenTableauDeMethod(DecacCompiler compiler) throws CodeGenError {
        LOG.debug("[DeclClass][codeGenTableauDeMethod] Generating the table method || ClassName =  " + 
                name.getName().getName()  + " // Super class  = " + superClass.getName().getName());
        LOG.debug("[DeclClass][codeGenTableauDeMethod] Class definitions are : \n Main class =  " + 
                    name.getClassDefinition().toString()  + " \n Super class  = " + superClass.getClassDefinition().toString());

        ClassDefinition classDefinition = name.getClassDefinition();
        // LEA 1 (GB), R0 
        ClassDefinition classDefinitionSuper = superClass.getClassDefinition();
        //On met l'adresse du debut de la table de méthode du parent dans le registre R0
        LOG.debug("[DeclClass][codeGenTableauDeMethod] Current methode table adresses \n" + compiler.getTableDeMethodeCompiler().toString()); 
        compiler.addInstruction(new LEA( compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(classDefinitionSuper),
                                         Register.getR(0)));

        compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().put(classDefinition, new RegisterOffset(compiler.incrementGbCompiler(), Register.GB));
        
        compiler.addInstruction(new STORE(Register.getR(0), compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(classDefinition)));
        

        MethodDefinition methodDefinitionIter;
        ClassDefinition currentMethodDefinition;
        for (int methodIndex = 1; methodIndex <= classDefinition.getNumberOfMethods(); methodIndex++){
            //On recupére la premier méthode avec l'index qui égal à methodIndex, ensuite on cherche la classe
            //auquel cette méthode appartient 
            methodDefinitionIter = classDefinition.getMembers().getMethodIndex(methodIndex);
            currentMethodDefinition = getClassdefinition(compiler, classDefinition.getMembers().getMethodEnvioremnent(
                                        methodDefinitionIter));
            methodDefinitionIter.setLabel(new Label("code."+
                                        currentMethodDefinition.getType().getName().getName()+"."+
                                        methodDefinitionIter.getMethodname()));
            compiler.addInstruction(new LOAD((DVal) new LabelOperand(methodDefinitionIter.getLabel()), Register.getR(0)));
            compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(compiler.incrementGbCompiler(), Register.GB)));
        }
    }

    @Override
    public void codeGenClassMethod(DecacCompiler compiler) throws CodeGenError {
        codeGenMethodInitialisation(compiler);
        methods.codeGenListMethod(compiler);
        classProgram.append(methods.getMethodsPrograms());
    }

    public void codeGenMethodInitialisation(DecacCompiler compiler) throws CodeGenError {
        setUpMethodDefinition(compiler);
        //We set the value of the stack to 0, and if we ever do function calls we add 2 to this field. In this case
        //we can only call one method directly which is the initialization of the parent, if other methods are called 
        // in the initialisation of the field, they will be added in an other part of the code   
        if (initMethodRegisterManagementUnit == null){
            LOG.info("[DeclClass][codeGenMethodInitialisation] Defining code initilization method");
            initMethodRegisterManagementUnit = new RegisterManagementUnit(compiler.getCompilerOptions().getNumberOfRegisters());
        }
        else{
            LOG.fatal("[DeclClass][codeGenMethodInitialisation] Trying to initialise the register management unit for the init method");
        }
        compiler.setMethodProgramState(initMethodRegisterManagementUnit, initMethodStackManagementUnit, initMethodProgram);
        //Si la classe parenet n'est pas object, on doit appeler sa méthode d'initialisation

        if (superClass.getName() != compiler.createSymbol("Object")){
            //On donne ajouter 3 à tempvariables car la méthode prend un paramétre et bsr a besoin de deux espaces
            //mémoire
            compiler.getRegisterManagement().increaseTempVariables(3);
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(0)), "Placing the current class = " + name.getName() +" adresse in the the stack before calling parent ");
            compiler.addInstruction(new PUSH(Register.getR(0)), "Placing this = " +name.getName()+" into the stack");
            compiler.addInstruction(new BSR(new Label("init." +superClass.getName().getName())), "Calling the initialisation method of parent class" + superClass.getName());
            compiler.addInstruction(new SUBSP(1)); //A FAIRE SChange value if needed
            compiler.getRegisterManagement().decreaseTempVariables(3);
        }   
        fields.CodeGenListPlaceZeroInField(compiler);
        fields.CodeGenListInitializeField(compiler);
        compiler.getRegisterManagement().pushUsedRegistersMethod(compiler);
        compiler.getRegisterManagement().popUsedRegistersMethod(compiler);
        compiler.addInstruction(new RTS()); 

        /**
         * Stack management
         */
        int sizeStackMax = compiler.getStackManagement().measureStacksizeNeededMethod(compiler);
        LOG.debug("[DeclClass][codeGenMethodInitialisation] sizeStackMax = " + sizeStackMax);
        if  (sizeStackMax !=0){
            compiler.getErrorManagementUnit().activeError("stack_overflow_error");
            if (!(compiler.getCompilerOptions().isNoCheck())) {
                compiler.getProgram().addFirst(new BOV(new Label("stack_overflow_error")));
                compiler.getProgram().addFirst(new TSTO(new ImmediateInteger(sizeStackMax)));
            }
        }

        //Adding function label  and comments for debugging purposes
        compiler.getProgram().addFirst(new Line(new Label("init."+name.getName() )));
        compiler.getProgram().addFirst(new Line("------------Init method for class = " + name.getName() +"--------"));
        compiler.getProgram().addFirst(new Line("---------------------------------------------------"));
        compiler.getProgram().addFirst(new Line("----------------- class : " + name.getName() +  " -------------------"));
        compiler.getProgram().addFirst(new Line("---------------------------------------------------"));
        classProgram.append(initMethodProgram);
    }   


    public void setUpMethodDefinition(DecacCompiler compiler) throws CodeGenError{
        MethodDefinition defintionMethodInitClass = new MethodDefinition(compiler.environmentType.VOID, Location.BUILTIN, 
                            new Signature(), name.getClassDefinition().getNumberOfMethods()+1);
        defintionMethodInitClass.setLabel(new Label("init."+name.getClassDefinition().getType().getName().getName()));
        
    }

    /**
     * Retourne la ClassDefinition associé au envExpr donnée en paramétre
     * @param compiler 
     * @param envExpr 
     * @return
     */
    public ClassDefinition getClassdefinition(DecacCompiler compiler, EnvironmentExp envExpr){
        for(ClassDefinition classDefinitionIter: compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().keySet()){
            if (classDefinitionIter.getMembers() == envExpr){
                return classDefinitionIter;
            }
        }
        return null;
    }

    @Override
    public IMAProgram getClassProgram() {
        return classProgram;
    }



    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass, ListDeclField fields, ListDeclMethod methods) {
        Validate.notNull(name);
        Validate.notNull(superClass);
        Validate.notNull(fields);
        Validate.notNull(methods);
        this.name = name ;
        this.superClass = superClass; 
        this.fields = fields;
        this.methods = methods;
    }

    public ListDeclField getFields(){
        return fields;
    }
    public ListDeclMethod getMethods() {
        return methods;
    }
    public AbstractIdentifier getName() {
        return name;
    }
    public AbstractIdentifier getSuperClass() {
        return superClass;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        s.print(getName().getName().getName());
        if(getSuperClass() != null && getSuperClass().getName().getName()!= "Object"){
            s.print(" extends ");
            s.print(getSuperClass().getName().getName());
        }
        s.println(" {");
        s.indent();
        getFields().decompile(s);
        getMethods().decompile(s);
        s.unindent();
        s.println("}");
    }



    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        if (!compiler.environmentType.getEnvTypes().containsKey(name.getName())){
            if (compiler.environmentType.getEnvTypes().containsKey(superClass.getName())){
                //Ce traitement associe à l'indetificateur la class super sa classe definition de object
                // Ceci sera utile pour le méthode de generation de code 
                superClass.setDefinition((ClassDefinition) compiler.environmentType.getEnvTypes().get(
                                                compiler.createSymbol(superClass.getName().getName())));
                ClassType classType = new ClassType(name.getName(), getLocation(), 
                                                    (ClassDefinition)compiler.environmentType.defOfType(superClass.getName()));
                compiler.environmentType.getEnvTypes().put(name.getName(), (ClassDefinition) classType.getDefinition());
                name.setDefinition((ClassDefinition) classType.getDefinition());
            }
            else
                throw new ContextualError("le super Class n'est pas déclaré", getLocation());   
        }
        else 
            throw new ContextualError("Double Déclaration de la class  " + name.getName().getName(), getLocation());
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
               ClassDefinition classDefi = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
               classDefi.setNumberOfFields(classDefi.getSuperClass().getNumberOfFields());
               classDefi.setNumberOfMethods(classDefi.getSuperClass().getNumberOfMethods());
               fields.verifyListDeclField(compiler, classDefi.getMembers(), classDefi);
               methods.verifyListDeclMethod(compiler, classDefi.getMembers(), classDefi);
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition classDefi = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
        fields.verifyInitFields(compiler, classDefi.getMembers(), classDefi);
        methods.verifyListDeclMethodBody(compiler, classDefi.getMembers(), classDefi);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false); 
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f) ;
        superClass.iter(f); 
        fields.iter(f);
        methods.iter(f);
    }


}
