package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManagementUnit;
import fr.ensimag.deca.codegen.StackManagementUnit;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.*;
import static org.mockito.Mockito.mockingDetails;
import org.apache.log4j.Logger;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.Register;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class DeclMethod extends AbstractDeclMethod {

    RegisterManagementUnit methodRegisterManagementUnit = null; // We need the number of registers, 
                                                 //  we can not intialize this value at this level
                                                 //  action will be done at the codegenmethod level 
    StackManagementUnit methodStackManagementUnit = new StackManagementUnit();
    IMAProgram methodProgram = new IMAProgram();

    public IMAProgram getMethodProgram() {
        return methodProgram;
    }

    public void setMethodProgram(IMAProgram methodProgram) {
        this.methodProgram = methodProgram;
    }

    private static final Logger LOG = Logger.getLogger(DeclMethod.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam listParam;
    final private AbstractMethodBody methodBody;


    public DeclMethod(AbstractIdentifier returntype, AbstractIdentifier methodName, ListDeclParam listParam, AbstractMethodBody methodBody ) {
        Validate.notNull(returntype);
        Validate.notNull(methodName);
        Validate.notNull(listParam);
        Validate.notNull(methodBody);
        this.type = returntype;
        this.methodName = methodName;
        this.listParam = listParam;
        this.methodBody = methodBody;

    }

    public AbstractIdentifier getType() {
        return type;
    }

    public AbstractIdentifier getMethodName() {
        return methodName;
    }

    public AbstractMethodBody getMethodBody() {
        return methodBody;
    }

    public ListDeclParam getListParam() {
        return listParam;
    }



    @Override
    public void decompile(IndentPrintStream s) {
        getType().decompile(s);
        s.print(" ");
        getMethodName().decompile(s);
        s.print("(");
        getListParam().decompile(s);
        s.print(")");
        getMethodBody().decompile(s);

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        listParam.iter(f);
        methodBody.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listParam.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }
    /**for the seconde passe  */
    @Override
    protected void verifyDeclMethodSimple(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("[DeclMethod][verifyDecleMethod] Verifing the declaration of a method in pass 2 || MethodName =  " + 
                                                    methodName.getName().getName());
        //r??cup??rer le type de return
        if (methodRegisterManagementUnit == null){
            methodRegisterManagementUnit = new RegisterManagementUnit(compiler.getCompilerOptions().getNumberOfRegisters());
        }
        else{
            LOG.fatal("[DeclMethod][verifyDeclMethodSimple] Trying to initialise the register management unit for a method");
        }
        compiler.setMethodProgramState(methodRegisterManagementUnit, methodStackManagementUnit, methodProgram);
        Type typeReturn = this.type.verifyType(compiler);
        this.type.setType(typeReturn);
        Signature signature=this.listParam.verifyListDeclParam(compiler);
        
        //pour ins??rer le nom de m??thode dans l'environement local
        if (localEnv.get(methodName.getName())==null){
            LOG.info("[DeclMethod][verifyDecleMethod] Method = "+ methodName.getName().getName()+ " does not exists,"
                        +" adding a new one to the local env with index "+ (currentClass.getNumberOfMethods()+1));
            MethodDefinition methodDef = new MethodDefinition(typeReturn, getLocation(), signature, currentClass.incNumberOfMethods());
            methodDef.setMethodname(methodName.getName().getName());
            methodName.setDefinition(methodDef); 
            try {
                localEnv.declare(methodName.getName(), methodName.getMethodDefinition());
            } catch (DoubleDefException e) {
                //not possible
            }
        }else{ //name is already declared, maybe in local maybe in the super-classes 
            //if it's in the local env:
            LOG.info("[DeclMethod][verifyDecleMethod] Method = "+ methodName.getName().getName()+ " exists in the localenv, "
                                    +"checking if it is a method that belongs to the current class");
            Map<Symbol, ExpDefinition> tempoMap = localEnv.getExp();
            if (tempoMap.containsKey(this.methodName.getName())){
                throw new ContextualError("Il existe d??j?? une m??thode ou un champs de nom " + methodName.getName().getName() + " dans la classe " + currentClass.getType().getName().getName(), getLocation());
            }else{
                LOG.info("[DeclMethod][verifyDecleMethod] Method = "+ methodName.getName().getName()+ " exists in the parent class,"+
                         "checking if our method matchs the one that is in the parent");
                //the same name is in the super class
                //v??rifier la condition de r??gle
                //get the parent's same-method-name signature:
                Signature sigSuper=localEnv.get(this.methodName.getName()).asMethodDefinition("sorry it's not a method", getLocation()).getSignature();
                Type typeSuperReturn=localEnv.get(this.methodName.getName()).asMethodDefinition("sorry it's not a method", getLocation()).getType();
                doubleDeclNameMethod(compiler, localEnv, sigSuper, signature, typeReturn, typeSuperReturn);

                int indexRecurringMethod =  ((MethodDefinition) (localEnv.get(methodName.getName()))).getIndex();
                MethodDefinition methodDef = new MethodDefinition(typeReturn, getLocation(), signature, indexRecurringMethod);
                methodDef.setMethodname(methodName.getName().getName());
                methodName.setDefinition(methodDef); 
                LOG.info("[DeclMethod][verifyDecleMethod] Method = "+ methodName.getName().getName()+" exists in the parent class,"+
                            "and it was redefined exactly the way it was in the parent method, we used the index the index =" + indexRecurringMethod);
                try {  
                    localEnv.declare(methodName.getName(), methodName.getMethodDefinition());
                } catch (DoubleDefException e) {
                    //forc??ment va marcher ici car on a d??j?? ??corch?? le cas d'erreur 
                    throw new ContextualError("[DeclMethod][verifyDeclMethodSimple]Bug compilateur", getLocation());
                }
            }
        }
    }

    /**
     *  A funtion for verify the constraints when the same name of method detected 
     * @param compiler
     * @param localEnv
     * @param sigSuper
     * @param sigCurrMethod
     * @param currTypeReturen
     * @param superTypeReturn
     * @throws ContextualError
     */
    private void doubleDeclNameMethod(DecacCompiler compiler, EnvironmentExp localEnv, 
                                        Signature sigSuper, Signature sigCurrMethod, 
                                        Type currTypeReturen , Type superTypeReturn) 
                                        throws ContextualError{
        //first verify they have the same signature
        int sizeSigSuper=sigSuper.size();
        if (sizeSigSuper != sigCurrMethod.size()){
            throw new ContextualError("Le nombre d'arguments de la signature de la m??thode " + methodName.getName().getName() + "  dans la classe actuelle n'est pas identique ?? celle de la super classe", getLocation());
        }
        for (int i=0;i<sizeSigSuper; ++i){
            if (!sigSuper.paramNumber(i).sameType(sigCurrMethod.paramNumber(i))){
                throw new ContextualError("Sorry the current signature is not the same of the super class's correspond signature", getLocation());
            }
        }
        //then verify the type return is the sub-type of corresponding super-class's types
        if (!currTypeReturen.isClass()){  //the primitif type
            if (!superTypeReturn.sameType(currTypeReturen))
                throw new ContextualError("Le type de retour de la methode d??clar??e n'a pas le m??me type que celui de sa super-classe alors que leur noms sont identiques ", getLocation());
        }else{
            //the current return type is a type of class 
            if (superTypeReturn.isClass()){
                if (((ClassType)currTypeReturen).isSubClassOf(((ClassType)superTypeReturn))){
                    return ;  // get out this fucntion without any exception 
                }else{
                    throw new ContextualError("Le type de retour de la methode d??clar??e n'est pas une sous classe de la m??thode avec le m??me nom en provenence de sa super classe ", getLocation());
                }
            }else{  //one classType one primitif type
                throw new ContextualError("Le type de retour de la methode d??clar??e est un type primitif mais sa m??thode de m??me nom de sa super classe est un type de classe", getLocation());
            }
        }
    }

    /**
     * for the passe 3 with the body something
     */
    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        compiler.setMethodProgramState(methodRegisterManagementUnit, methodStackManagementUnit, methodProgram);
        listParam.getenvParm().setParentEnvironment(localEnv);
        methodBody.verifyDeclMethodBody(compiler, listParam.getenvParm(), currentClass, type.getType());              
    }



    public void codeGenDeclMethod(DecacCompiler compiler) throws CodeGenError {
        compiler.setMethodProgramState(methodRegisterManagementUnit, methodStackManagementUnit, methodProgram);   
        /*
         * Ajout du label de la forme fin.<classname>.<methodname>
         */
        StringBuilder endMethod = new StringBuilder();
        endMethod.append(methodName.getMethodDefinition().getLabel().toString());
        endMethod.replace(0, 4, "fin");
        String destFile = endMethod.toString() ;
        methodName.getMethodDefinition().setEndLabel(new Label(destFile));
        compiler.setCurrentMethodCodeGen(methodName.getMethodDefinition());
        /**
         * Generating ainstruction for the method
         */
        methodBody.codeGenMethodBody(compiler);
        compiler.getRegisterManagement().pushUsedRegistersMethod(compiler);
        addReturnError(compiler);   
        compiler.addLabel(methodName.getMethodDefinition().getEndLabel());
        compiler.getRegisterManagement().popUsedRegistersMethod(compiler);
        compiler.addInstruction(new RTS()); 
        /**
         * Stack management
         */
        int sizeStackMax = compiler.getStackManagement().measureStacksizeNeededMethod(compiler);
        LOG.debug("[DeclClass][codeGenMethodInitialisation] sizeStackMax = " + sizeStackMax);
        if  (sizeStackMax !=0){
            /**
             * On ajoute la v??rification d'espace dans le stack
             */
            compiler.getErrorManagementUnit().activeError("stack_overflow_error");
            if (compiler.getStackManagement().getLbCounter() !=0){
                compiler.getProgram().addFirst(new ADDSP(new ImmediateInteger(compiler.getStackManagement().getLbCounter())));
            }
            if (!(compiler.getCompilerOptions().isNoCheck())) {
                compiler.getProgram().addFirst(new BOV(new Label("stack_overflow_error")));
                compiler.getProgram().addFirst(new TSTO(new ImmediateInteger(sizeStackMax)));
            }
        }

        //Adding function label  and comments for debugging purposes
        compiler.getProgram().addFirst(new Line(methodName.getMethodDefinition().getLabel()));
        compiler.getProgram().addFirst(new Line("------------Code for method : " + methodName.getName() + " with label =  " +
                                                 methodName.getMethodDefinition().getLabel().toString() +"--------"));
    }  


    protected void addReturnError(DecacCompiler compiler){
        if (!(compiler.getCompilerOptions().isNoCheck())){
            if (methodName.getMethodDefinition().getType() != compiler.environmentType.VOID ){
                compiler.addInstruction(new WSTR("Error: La m??thode "+ methodName.getMethodDefinition().getLabel().toString() + " doit retourner un element"));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
            }
        }
    }

}

