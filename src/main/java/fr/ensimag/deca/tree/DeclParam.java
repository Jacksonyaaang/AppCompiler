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
}

