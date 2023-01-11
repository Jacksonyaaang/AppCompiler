package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl15
 * @date 01/01/2023
 */
public class DeclVar extends AbstractDeclVar {

    
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
        try{
            Type t = type.verifyType(compiler);
            type.setType(t);
            if(t.isVoid()) {
                throw new ContextualError("Le type ne peut pas être void", getLocation());
            }
            initialization.verifyInitialization(compiler, type.getType(), localEnv, currentClass);
            VariableDefinition VDf = new VariableDefinition(type.getType(), varName.getLocation());
            varName.setDefinition(VDf);
            localEnv.declare(varName.getName(), varName.getExpDefinition());
        }catch (ContextualError e){
            e.fillInStackTrace();
        }catch (EnvironmentExp.DoubleDefException e){
            throw new ContextualError("double declaration", varName.getLocation());
        }

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
