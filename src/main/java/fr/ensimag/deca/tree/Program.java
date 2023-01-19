package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        if (compiler.getRegisterManagement() == null){
            LOG.debug("Register unit management unit is null");
        }
        else
        {
            LOG.debug("Register unit management unit not null");
        }
        compiler.saveMainProgramState();
        classes.verifyListClass(compiler);
        classes.verifyListClassMembers(compiler);
        classes.verifyListClassBody(compiler);
        compiler.setMainProgramState();
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) throws CodeGenError{
        compiler.addComment("-------------------------------------");
        compiler.addComment("       Method Table");
        compiler.addComment("-------------------------------------");
        compiler.setMainProgramState();
        generateMethodTableForObjectClass(compiler);
        //Generation de code class
        classes.codeGenListClassTableau(compiler);
        //Code class end 
        compiler.setMainProgramState();
        compiler.addComment("-------------------------------------");
        compiler.addComment("       Main program");
        compiler.addComment("-------------------------------------");
        classes.codeGenListClassMethod(compiler);
        compiler.setMainProgramState();
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());

        // Ajoute de code de class et la liste des méthodes
        compiler.addComment("-------------------------------------");
        compiler.addComment("       Classes");
        compiler.addComment("-------------------------------------");
        compiler.getProgram().append(classes.getClassesProgram());
        compiler.addComment("-------------------------------------");
        compiler.addComment("       Liste Erreur");
        compiler.addComment("-------------------------------------");
        compiler.getErrorManagementUnit().writeListError(compiler);
        compiler.addLabel(new Label("code.object.equals"));
        compiler.addInstruction(new RTS()); 
    }

    public void generateMethodTableForObjectClass(DecacCompiler compiler) throws CodeGenError{
        ClassDefinition objectClassDefinition = (ClassDefinition) compiler.environmentType.getEnvTypes().get(compiler.createSymbol("Object"));
        compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().put(objectClassDefinition, new RegisterOffset(compiler.incrementGbCompiler(), Register.GB));
        compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(objectClassDefinition)));
        compiler.addInstruction(new LOAD((DVal) new LabelOperand(new Label("code."+objectClassDefinition.getType().getName().getName()+".equals")),
                                                 Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(compiler.incrementGbCompiler(), Register.GB)));
    }
    public void generateCodeForObjectEquals(DecacCompiler compiler){
        compiler.addComment("------------------Start generateCodeForObjectEquals location:"+getLocation()+"-----------------");
        Label fin_equals=new Label("FIN_equals");
        compiler.addLabel(new Label("code.Object.equals"));
        compiler.addInstruction(new TSTO(1),"TEST de débordement de Pile");
        compiler.addInstruction(new BOV(new Label("stack_overflow_error")));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(0)));// (@this -> R2)
        compiler.addInstruction(new CMP(new RegisterOffset(-3, Register.LB), Register.getR(0)));
        compiler.addInstruction(new SEQ(Register.getR(0)),"Comparaison this et paramètre d'equals");
        compiler.addLabel(fin_equals);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
