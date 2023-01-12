package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterMangementUnit;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.*;
import static org.mockito.Mockito.mockingDetails;
import org.apache.log4j.Logger;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.Register;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class DeclVar extends AbstractDeclVar {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }
    

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
        
        Type t = type.verifyType(compiler);
        type.setType(t);
        if(t.isVoid()) {
            throw new ContextualError("Le type ne peut pas être void", getLocation());
        }
        initialization.verifyInitialization(compiler, type.getType(), localEnv, currentClass);
        VariableDefinition VDf = new VariableDefinition(type.getType(), varName.getLocation());
        varName.setDefinition(VDf);
        try{
            localEnv.declare(varName.getName(), varName.getExpDefinition());
        } 
        catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(e.getMessage(), getLocation());
        }
        //On associe à la variable défini une adresse dans le stack avec une adresse X(Gb)
        this.varName.getExpDefinition().setOperand(new RegisterOffset(compiler.incrementGbCompiler(), Register.GB)); 
        LOG.debug("Saving " + this.varName.getName() + " into " + this.varName.getExpDefinition().getOperand());
        //System.out.println("[DeclVar] Saving " + this.varName.getName() + " into " + this.varName.getExpDefinition().getOperand());
    }

    @Override
    public void codeGenDecl(DecacCompiler compiler) throws CodeGenError {
        //A FAIRE, TRAITER LE CAS AU QUEL ON TRAVAIL AVEC UN FLOAT = INT
        if (initialization instanceof Initialization ){
            this.initialization.codegenInitial(compiler);
            compiler.addInstruction(new STORE(this.initialization.getRegistreDeRetour(),
                                        varName.getExpDefinition().getOperand()), "Initializing the variable "+ getVarName().getName()
                                                                +" and loading it into memory"); 
        }
        //Si la variable n'est pas initializée on ne fait rien
    }

    public AbstractIdentifier getType() { return type; }

    public AbstractIdentifier getVarName() { return varName; }

    public AbstractInitialization getInitialization() { return initialization; }

    @Override
    public void decompile(IndentPrintStream s) {
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
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
