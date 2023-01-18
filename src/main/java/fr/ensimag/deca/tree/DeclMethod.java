package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterMangementUnit;
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
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class DeclMethod extends AbstractDeclMethod {

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
        //récupérer le type de return 
        Type typeReturn = this.type.verifyType(compiler);
        this.type.setType(typeReturn);
        Signature signature=this.listParam.verifyListDeclParam(compiler);
        
        //pour insérer le nom de méthode dans l'environement local
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
                throw new ContextualError("Il existe déjà une méthode de même nom "+methodName.getName().getName() + " dans la classe" + currentClass.getType().getName().getName(), getLocation());
            }else{
                LOG.info("[DeclMethod][verifyDecleMethod] Method = "+ methodName.getName().getName()+ " exists in the parent class,"+
                         "checking if our method matchs the one that is in the parent");
                //the same name is in the super class
                //vérifier la condition de règle
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
                    //forcément va marcher ici car on a déjà écorché le cas d'erreur 
                    throw new ContextualError("Bug compilateur", getLocation());
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
            throw new ContextualError("La signature de la méthode " + methodName.getName().getName() + "  dans la classe actuelle n'est pas identique à celle de la super classe", getLocation());
        }
        for (int i=0;i<sizeSigSuper; ++i){
            if (!sigSuper.paramNumber(i).sameType(sigCurrMethod.paramNumber(i))){
                throw new ContextualError("La signature de la méthode "  + methodName.getName().getName() + " dans la classe actuelle n'est pas identique à celle de la super classe", getLocation());
            }
        }
        //then verify the type return is the sub-type of corresponding super-class's types
        if (!currTypeReturen.isClass()){  //the primitif type
            if (!superTypeReturn.sameType(currTypeReturen))
                throw new ContextualError("[ERROR] The current method don't have a proper type", getLocation());
        }else{
            //the current return type is a type of class 
            if (superTypeReturn.isClass()){
                if (((ClassType)currTypeReturen).isSubClassOf(((ClassType)superTypeReturn))){
                    return ;  // get out this fucntion without any exception 
                }else{
                    throw new ContextualError("[ERROR] The current method don't have a proper type", getLocation());
                }
            }else{  //one classType one primitif type
                throw new ContextualError("[ERROR] The current method don't have a proper type", getLocation());
            }
        }
    }

    /**
     * for the passe 3 with the body something
     */
    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        listParam.getenvParm().setParentEnvironment(localEnv);
        methodBody.verifyDeclMethodBody(compiler, listParam.getenvParm(), currentClass, type.getType());      
        // TODO Auto-generated method stub
        
    }

}

