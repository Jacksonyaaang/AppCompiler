package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
<<<<<<< HEAD
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.codegen.CodeGenError;
import org.apache.log4j.Logger;

=======
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
>>>>>>> remotes/origin/victor
/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Assign extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(Assign.class);

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

<<<<<<< HEAD
    private static final Logger LOG = Logger.getLogger(IntLiteral.class);
=======
    // protected void codeGenInst(DecacCompiler compiler) {
    //     compiler.addInstruction();
    // }

>>>>>>> remotes/origin/victor

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultregister){
        LOG.debug("[Assign][executeBinaryOperation] generating code for int literal value " );
        System.out.println("[Assign][executeBinaryOperation] generating code for assignement of: " 
                        +val + " to " + resultregister);
        compiler.addInstruction(new LOAD(val, resultregister));
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) throws CodeGenError {
            this.getRightOperand().codeGenInst(compiler);
            assert( this.getRightOperand().getRegisterDeRetour() != null);
            System.out.println(" Left is being stored at " + ((Identifier) getLeftOperand()).getExpDefinition().getOperand());
            assert(((Identifier) getLeftOperand()).getExpDefinition().getOperand() != null);
            assert(getLeftOperand() instanceof Identifier);
            LOG.debug(" Assiging a value to " + ((Identifier) getLeftOperand()).getName());
            System.out.println(" Assiging a value to " + ((Identifier) getLeftOperand()).getName());
            compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),
                                            ((Identifier) getLeftOperand()).getExpDefinition().getOperand()),                                          
                                            " Assiging a value to " + ((Identifier) getLeftOperand()).getName()); 
    }
    

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        System.out.println("On est dans Assign.java");
        Type typOpLeft = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(getRightOperand() instanceof AbstractReadExpr))
            getRightOperand().verifyRValue(compiler, localEnv, currentClass, typOpLeft);
        else{
            Type typOpRight = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            if (!typOpLeft.sameType(typOpRight))
                throw new ContextualError("can't affecte read to this variable", getLocation());
        }
        setType(typOpLeft);
        return getType();
        //return getLeftOperand().getType();
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
