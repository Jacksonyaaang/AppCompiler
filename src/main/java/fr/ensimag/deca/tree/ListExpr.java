package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl15
 * @date 01/01/2023
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        if (getList().size() != 0){
            AbstractExpr e1 = getList().get(0);
            e1.decompile(s);
            for( int i = 1; i<getList().size(); i++){
                s.print(", ");
                AbstractExpr e = getList().get(i);
                e.decompile(s);
            }
        }
    }
}
