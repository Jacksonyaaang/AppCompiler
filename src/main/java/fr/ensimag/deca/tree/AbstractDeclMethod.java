package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public abstract class AbstractDeclMethod extends Tree {

    AbstractIdentifier type;
    AbstractIdentifier name;
    ListDeclParam params;
    MethodBody body;

    public AbstractIdentifier getType() {
        return type;
    }

    public AbstractIdentifier getName() {
        return name;
    }

    public ListDeclParam getParams() {
        return params;
    }

    public MethodBody getBody() {
        return body;
    }

}
