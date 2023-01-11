package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import org.apache.log4j.Logger;
    

/**
 * Binary expressions.
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    private static final Logger LOG = Logger.getLogger(IntLiteral.class);

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * Cette méthode est utilisée pour plusieurs operations binaires, par exemple les operation 
     * arithmétique, inégalitée, égalité //A FAIRE SÉPCIFIER le reste
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{    
        LOG.debug("[AbstractBinaryExpr][codeGenInst] generating code for abstact binary expression ");
        System.out.println("[AbstractBinaryExpr][codeGenInst] ---- Start ---- Generating code for abstact binary expression");
        System.out.println(compiler.getRegisterManagement());
        System.out.println("[AbstractBinaryExpr][codeGenInst] Exploring Left");
        LOG.debug("[AbstractBinaryExpr][codeGenInst] Exploring Left");
        getLeftOperand().codeGenInst(compiler);
        System.out.println("Left register " + getLeftOperand().getRegisterDeRetour());
        
        //La méthode dans le if, vérifie s'il s'agit d'un identificateur et dans ce cas 
        //elle exploitra les adresses.
        if (rightOperandIdentifier(compiler, rightOperand) != null){
            this.executeBinaryOperation( compiler, ((Identifier) getRightOperand()).getExpDefinition().getOperand(), 
                                    getLeftOperand().getRegisterDeRetour());
        }
        else{
            if (getRightOperand().getRegisterDeRetour() != getLeftOperand().getRegisterDeRetour()){
                this.executeBinaryOperation( compiler, getRightOperand().getRegisterDeRetour(),
                        getLeftOperand().getRegisterDeRetour());
            }
            else{
                //Si jamais les registres sont les même, cela veux dire que un registre est dans le stack et l'autre est utilisée
                //Dans ce cas, on fait un pop du registre à droite et on le met dans le registre R0 pour faire le calcule dedans
                //ensuite on fera un load qur ce qui est dans le registre initial
                if (getRightOperand().getRegisterToPop().size() != 1) {
                    throw new CodeGenError("La taille du stack dans l'operand droite doit être égal à 0");
                }
                getRightOperand().getRegisterToPop().pop(); //On elimine le variable du stack de l'expression, 
                                                            //afin de pourvoir réduire le nombre d'operation
                compiler.addInstruction(new POP(Register.getR(0)), getOperatorName());
                this.executeBinaryOperation( compiler, getRightOperand().getRegisterDeRetour(),
                        getLeftOperand().getRegisterDeRetour());
            }
        }
        //Dans ce cas l'a on utilise des valeurs temporaires, dans ce cas on n'a pas de registe libre
        // ainsi on retourne le resultat dans le registre R0 afin de ne pas perdre d'inforamtion
        if (getLeftOperand().getRegisterToPop().size() !=0){
            compiler.addInstruction(new LOAD(getLeftOperand().getRegisterDeRetour(), Register.getR(0)) , "Using R0 register to return values since other registers are being used");
            this.setRegisterDeRetour(Register.getR(0));
        }
        else{
            this.setRegisterDeRetour(getLeftOperand().getRegisterDeRetour());
        }
        getRightOperand().popRegisters(compiler);
        getLeftOperand().popRegisters(compiler);
        compiler.getRegisterManagement().freeRegister(getLeftOperand().getRegisterDeRetour());
    }

    /**
     * Cette méthode est utilisée par les classes qui hérite cette méthodes afin de pouvoir s'ajuster 
     * au different type d'operation binaire (par exemple addition, )
     * @param compiler
     * @param val
     * @param resultregister
     */
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultregister) throws CodeGenError {
        throw new CodeGenError("This method should not be visited");
    }

    private DAddr rightOperandIdentifier(DecacCompiler compiler,AbstractExpr expr) throws CodeGenError {
       if (expr instanceof Identifier) {
            return ((Identifier) expr).getExpDefinition().getOperand();
        }
        System.out.println("[AbstractBinaryExpr][codeGenInst] Exploring Right");
        LOG.debug("[AbstractBinaryExpr][codeGenInst] Exploring Right");           
        getRightOperand().codeGenInst(compiler);
        System.out.println("Right register equal " + getRightOperand().getRegisterDeRetour());
        return null;
    }



    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

}
