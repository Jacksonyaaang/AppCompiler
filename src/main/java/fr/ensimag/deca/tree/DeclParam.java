package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManagementUnit;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.*;
import static org.mockito.Mockito.mockingDetails;
import org.apache.log4j.Logger;
import java.io.PrintStream;
import java.util.Map;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;


/**
 * @author gl15
 * @date 01/01/2023
 */
public class DeclParam extends AbstractDeclParam {


    
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        Validate.notNull(type);
        Validate.notNull(paramName);
        this.type = type;
        this.paramName = paramName;
    }
    


    public AbstractIdentifier getType() { return type; }

    public AbstractIdentifier getParamName() { return paramName; }


    @Override
    public void decompile(IndentPrintStream s) {
        getType().decompile(s);
        s.print(" ");
        getParamName().decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }


    /**
     * for the pass 2 of partie B
     * the 'type' in return value is for construting the signature
     */
    @Override
    protected Type verifyDeclParam(DecacCompiler compiler, EnvironmentExp envParms)
            throws ContextualError {
            LOG.debug("[DeclParam][verifyDecleParam] Verifing method parameter : "+paramName.getName());
            Type typeParam = type.verifyType(compiler);
            type.setType(typeParam);
            //make sure this type isn't Void
            if (typeParam.isVoid()){
                throw new ContextualError("Le type void ne peut être un type d'une méthode", getLocation());
            }
            //verify environmentTypes has the type of Paramname 
            Map<Symbol, TypeDefinition> envTypes = compiler.environmentType.getEnvTypes();
            if (!(envTypes.containsKey(typeParam.getName()))){
                throw new ContextualError("Le type " + typeParam.getName() + " ne peut être utilisée comme paramétre de fonction", getLocation());
            }
            ParamDefinition paramDef = new ParamDefinition(typeParam, getLocation());
            paramName.setDefinition(paramDef);
            try {
                envParms.declare(paramName.getName(), paramDef);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError("Il existe déjà un paramètre de nom " + paramName.getName().getName() + " dans la même méthode ", getLocation());
            }
            this.paramName.getExpDefinition().setOperand(new RegisterOffset(compiler.decrementParamCounterCompiler(), Register.LB)); 
            LOG.debug("[DeclParam][verifyDecleParam] Saving method parameter " + this.paramName.getName() + " into " + this.paramName.getExpDefinition().getOperand());
            return typeParam;  //il faut le 'type' comme la type de return pour charger la signature 

        }

}





