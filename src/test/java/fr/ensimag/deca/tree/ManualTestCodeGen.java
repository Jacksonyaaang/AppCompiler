

package fr.ensimag.deca.tree;

import fr.ensimag.deca.syntax.*;
import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractProgram;
import java.io.File;
import java.io.IOException;
import org.antlr.v4.runtime.CommonTokenStream;


/* A manual test for testing code generation for the compiler
*/

/**
 *
 * @author Mehdi
 * @date 01/01/2023
 */

public class ManualTestCodeGen {
    public static void main(String[] args) throws IOException {
        // Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        File file = null;
        if (lex.getSourceName() != null) {
            file = new File(lex.getSourceName());
        }
        final DecacCompiler decacCompiler = new DecacCompiler(new CompilerOptions(), file);
        parser.setDecacCompiler(decacCompiler);
        AbstractProgram prog = parser.parseProgramAndManageErrors(System.err);
        if (prog == null) {
            System.exit(1);
        } else {
            prog.codeGenProgram(decacCompiler);
            String result = decacCompiler.displayIMAProgram();
            System.out.println(result);
        }

    }
    
}



