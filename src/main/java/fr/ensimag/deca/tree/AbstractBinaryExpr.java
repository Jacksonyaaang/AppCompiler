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
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
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

    private Boolean workWithFloats = false;
    private Boolean convNeeded = false;

    public Boolean getConvNeeded() {
        return convNeeded;
    }

    public void setConvNeeded(Boolean convNeeded) {
        this.convNeeded = convNeeded;
    }

    public Boolean getWorkWithFloats() {
        return workWithFloats;
    }

    public void setWorkWithFloats(Boolean workWithFloats) {
        this.workWithFloats = workWithFloats;
    }

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
     * codeGenInst Cette méthode est utilisée pour plusieurs operations binaires, par exemple les operation 
     * arithmétique, inégalitée, égalité, operation d'inégalité
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{    
        LOG.debug("[AbstractBinaryExpr][codeGenInst] generating code for abstact binary expression ");
        //LOG.debug(compiler.getRegisterManagement());
        LOG.debug("[AbstractBinaryExpr][codeGenInst] Exploring Left");
        checkIfWeWorkWithFloatAndIfConvIsNeeded(compiler);
        getLeftOperand().codeGenInst(compiler);
        LOG.debug(" [AbstractBinaryExpr][codeGenInst] Left register " + getLeftOperand().getRegisterDeRetour());
        //S'il s'agit d'un identificateur, on fait un traitement spécial qui nous fait gagners
        if (rightOperandIdentifier(compiler, rightOperand) != null){
            this.executeBinaryOperation( compiler, ((Identifier) getRightOperand()).getExpDefinition().getOperand(), 
                                    getLeftOperand().getRegisterDeRetour());
            this.transferPopRegisters(getLeftOperand().getRegisterToPop());
            this.setRegisterDeRetour(getLeftOperand().getRegisterDeRetour());
            return ;
        }
        else{
            if (getRightOperand().getRegisterDeRetour() != getLeftOperand().getRegisterDeRetour()){
                this.executeBinaryOperation( compiler, getRightOperand().getRegisterDeRetour(),
                        getLeftOperand().getRegisterDeRetour());
            }
            else{
                throw new CodeGenError("Should never have equal registers with the new approch; this must never be called");
            }
        }
        if (this.getRegisterDeRetour() == null) {
            this.setRegisterDeRetour(getLeftOperand().getRegisterDeRetour());
        }
        getRightOperand().popRegisters(compiler);
        this.transferPopRegisters(getLeftOperand().getRegisterToPop());
        compiler.getRegisterManagement().decrementOccupationRegister(getRightOperand().getRegisterDeRetour());
    }

    public void checkIfWeWorkWithFloatAndIfConvIsNeeded(DecacCompiler compiler){
        LOG.debug("[AbstractBinaryExpr][checkIfWeWorkWithFloatAndIfConvIsNeeded] Conv float is needed in this binary operation");
        if (getLeftOperand().getType() == compiler.environmentType.FLOAT 
            && getRightOperand().getType() == compiler.environmentType.FLOAT ){
            workWithFloats = true;
            //convNeeded = false;
        }
        if (getLeftOperand().getType() == compiler.environmentType.INT 
            && getRightOperand().getType() == compiler.environmentType.FLOAT ){
            workWithFloats = true;
            //convNeeded = true;
        }
        if (getLeftOperand().getType() == compiler.environmentType.FLOAT 
            && getRightOperand().getType() == compiler.environmentType.INT ){
            workWithFloats = true;
            //convNeeded = true;
        }
        if (getLeftOperand().getType() == compiler.environmentType.INT 
            && getRightOperand().getType() == compiler.environmentType.INT ){
            workWithFloats = false;
            //convNeeded = false;
        }
    }

    public void addConvertInstructions(DecacCompiler compiler){
        // if (getLeftOperand().getType() == compiler.environmentType.INT ){
        //     compiler.addInstruction(new FLOAT(getLeftOperand().getRegisterDeRetour(), getLeftOperand().getRegisterDeRetour()), "Converting left operand into a Float");            
        // }
        // if (getRightOperand().getType() == compiler.environmentType.INT ){
        //     compiler.addInstruction(new FLOAT(getRightOperand().getRegisterDeRetour(), getRightOperand().getRegisterDeRetour()), "Converting right operand into a Float");            
        // }
    }

    /** 
     * Cette méthode est utilisée par les classes qui hérite cette méthodes afin de pouvoir s'ajuster 
     * au different type d'operation binaire (par exemple addition, )
     * @param compiler
     * @param val
     * @param resultRegister
     */
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        throw new CodeGenError("This method should not be visited");
    }

    private DAddr rightOperandIdentifier(DecacCompiler compiler,AbstractExpr expr) throws CodeGenError {
        if (!convNeeded && !(this instanceof Divide) && expr instanceof Identifier) {
            return ((Identifier) expr).getExpDefinition().getOperand();
        }
        //System.out.println("[AbstractBinaryExpr][codeGenInst] Exploring Right");
        LOG.debug("[AbstractBinaryExpr][codeGenInst] Exploring Right");           
        getRightOperand().codeGenInst(compiler);
        //System.out.println("Right register equal " + getRightOperand().getRegisterDeRetour());
        LOG.debug("Right register equal " + getRightOperand().getRegisterDeRetour());
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

//A FAIRE : DELETE THIS 
//Si jamais les registres sont les même, cela veux dire que un registre est dans le stack et l'autre est utilisée
//Dans ce cas, on fait un pop du registre à droite et on le met dans le registre R0 pour faire le calcule dedans
// //ensuite on fera un load qur ce qui est dans le registre initial
// System.out.println("[AbstractBinaryExpr][codeGenInst] Right, this register is being used twice  " +
//                     getLeftOperand().getRegisterDeRetour());
// LOG.debug("[AbstractBinaryExpr][codeGenInst] Right, this register is being used twice  " +
//                                     getLeftOperand().getRegisterDeRetour());

// System.out.println("[AbstractBinaryExpr][codeGenInst] Right, size of register stack " +
//                     getRightOperand().getRegisterToPop().size() + " and the peek register is equal to  "
//                     + getRightOperand().getPeekRegisterToPop());
// LOG.debug("[AbstractBinaryExpr][codeGenInst] Right, size of register stack " 
//              + getRightOperand().getRegisterToPop().size() + " and the peek register is equal to  "
//             + getRightOperand().getPeekRegisterToPop());       

// if (!(getRightOperand().getRegisterToPop().size() != 1 || getRightOperand().getRegisterToPop().size() != 0)) {
//     throw new CodeGenError("La taille du stack dans l'operand droite doit être égal à 1 ou 0 , actual size = " + 
//                                     getRightOperand().getRegisterToPop().size() ); 
// }import fr.ensimag.ima.pseudocode.RegisterOffset;

// else if (getRightOperand().getRegisterToPop().size()  == 1){
//     if (getRightOperand().getPeekRegisterToPop() != getLeftOperand().getRegisterDeRetour()){
//         throw new CodeGenError("Dans le cas ou le Le registre sont égaux, dans le stack du registre à droite "+
//         "on doit avoir le registre de droite sinon on ne pas faire le pop" + 
//                             getRightOperand().getRegisterDeRetour()); 
//     }
//     getRightOperand().getRegisterToPop().pop(); 
//     compiler.addInstruction(new POP(Register.getR(0)), "Poping the value stored in the stack(left operand) and " 
//                                 + "placing in R0 ");
//     this.executeBinaryOperation(compiler, getLeftOperand().getRegisterDeRetour(), Register.getR(0));
//     this.setRegisterDeRetour(Register.getR(0));
//     //On elimine le variable du stack de l'expression, 
//     //afin de pourvoir réduire le nombre d'operation
// }
// else{
//     throw new CodeGenError("Data has been lost in the register " + 
//              getRightOperand().getRegisterDeRetour() ); 
// }
// // if (!(getLeftOperand().getRegisterToPop().size() != 1 || getLeftOperand().getRegisterToPop().size() != 0)) {
// //     throw new CodeGenError("La taille du stack dans l'operand gauche doit être égal à 1, actual size = " + 
// //                                     getLeftOperand().getRegisterToPop().size() );
// // }
// // else if (getLeftOperand().getRegisterToPop().size()  == 1) {
// //     getLeftOperand().getRegisterToPop().pop(); 
// // }


//compiler.addInstruction(new LOAD(getLeftOperand().getRegisterDeRetour(), Register.getR(0)), "Placing right operand in R0 registre");
//compiler.addInstruction(new POP(getLeftOperand().getRegisterDeRetour()), "Poping the value stored in the stack  and " 
//  