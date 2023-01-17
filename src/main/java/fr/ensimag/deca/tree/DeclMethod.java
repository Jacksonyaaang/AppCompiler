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

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

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
        //A FAIRE DECOMPILE 
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
        LOG.debug("[DeclMethod][verifyDecleMethod] Verify a method of class declaration");
        //récupérer le type de return 
        Type typeReturn = this.type.verifyType(compiler);
        this.type.setType(typeReturn);

        Signature signature=this.listParam.verifyListDeclParam(compiler);
        
        currentClass.incNumberOfMethods();
        int index = currentClass.getNumberOfMethods();  //already increase the number of methods
        MethodDefinition methodDef = new MethodDefinition(typeReturn, getLocation(), signature, index);
        methodName.setDefinition(methodDef);
        //pour insérer le nom de méthode dans l'environement local
        if (localEnv.get(methodName.getName())==null){
            //jamais le déclarer avant 
            try {
                localEnv.declare(methodName.getName(), methodName.getExpDefinition());
            } catch (DoubleDefException e) {
                //not possible
            }
        }else{ //name is already declared, maybe in local maybe in the super-classes 
            //if it's in the local env:
            Map<Symbol, ExpDefinition> tempoMap = localEnv.getExp();
            if (tempoMap.containsKey(this.methodName.getName())){
                throw new ContextualError("[ERROR] We got the same method name in the current class bro!", getLocation());
            }else{
                //the same name is in the super class
                //vérifier la condition de règle
                //get the parent's same-method-name signature:
                Signature sigSuper=localEnv.get(this.methodName.getName()).asMethodDefinition("sorry it's not a method", getLocation()).getSignature();
                Type typeSuperReturn=localEnv.get(this.methodName.getName()).asMethodDefinition("sorry it's not a method", getLocation()).getType();
                doubleDeclNameMethod(compiler, localEnv, sigSuper, signature, typeReturn, typeSuperReturn);
                try {  
                    localEnv.declare(methodName.getName(), methodName.getExpDefinition());
                } catch (DoubleDefException e) {
                    //forcément va marcher ici car on a déjà écorché le cas d'erreur 
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
            throw new ContextualError("Sorry the current signature is not the same of the super class's correspond signature", getLocation());
        }
        for (int i=0;i<sizeSigSuper; ++i){
            if (!sigSuper.paramNumber(i).sameType(sigCurrMethod.paramNumber(i))){
                throw new ContextualError("Sorry the current signature is not the same of the super class's correspond signature", getLocation());
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
        // TODO Auto-generated method stub
        
    }

}

