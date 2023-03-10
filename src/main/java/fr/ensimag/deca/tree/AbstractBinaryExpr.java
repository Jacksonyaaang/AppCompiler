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

    public Boolean getWorkWithFloats() {
        return workWithFloats;
    }

    public void setWorkWithFloats(Boolean workWithFloats) {
        this.workWithFloats = workWithFloats;
    }

    private static final Logger LOG = Logger.getLogger(AbstractBinaryExpr.class);

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
     * codeGenInst Cette m??thode est utilis??e pour plusieurs operations binaires, par exemple les operation 
     * arithm??tique, in??galit??e, ??galit??, operation d'in??galit??
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{    
        LOG.debug("[AbstractBinaryExpr][codeGenInst] generating code for abstact binary expression ");
        //LOG.debug(compiler.getRegisterManagement());
        compiler.addComment("--------StartBinaryOp--------"+getLocation()+"-----");
        LOG.debug("[AbstractBinaryExpr][codeGenInst] Exploring Left");
        checkIfWeWorkWithFloatAndIfConvIsNeeded(compiler);
        getLeftOperand().codeGenInst(compiler);
        LOG.debug(" [AbstractBinaryExpr][codeGenInst] Left register " + getLeftOperand().getRegisterDeRetour());
        
        //S'il s'agit d'un identificateur dans l'operand droit, on fait un traitement sp??cial 
        //qui exploite l'adresse de l'indentificateur
        if (rightOperandIdentifier(compiler, rightOperand) != null){
            LOG.debug("[AbstractBinaryExpr][codeGenInst] Exploiting an identifier with the adresse" +
                                     ((Identifier) getRightOperand()).getExpDefinition().getOperand());
            this.executeBinaryOperation( compiler, ((Identifier) getRightOperand()).getExpDefinition().getOperand(), 
                                    getLeftOperand().getRegisterDeRetour());
            this.transferPopRegisters(getLeftOperand().getRegisterToPop());
            this.setRegisterDeRetour(getLeftOperand().getRegisterDeRetour());
            compiler.addComment("--------EndBinaryOp--------"+getLocation()+"-----");
            return ;
        }
        else{
            if (getRightOperand().getRegisterDeRetour() != getLeftOperand().getRegisterDeRetour()){
                this.executeBinaryOperation( compiler, getRightOperand().getRegisterDeRetour(),
                        getLeftOperand().getRegisterDeRetour());
            }
            else{
                throw new CodeGenError(getLocation(), "Should never have equal registers with this algorithm; this must never be called");
            }
        }
        if (this.getRegisterDeRetour() == null) {
            this.setRegisterDeRetour(getLeftOperand().getRegisterDeRetour());
        }
        getRightOperand().popRegisters(compiler);
        this.transferPopRegisters(getLeftOperand().getRegisterToPop());
        compiler.getRegisterManagement().decrementOccupationRegister(getRightOperand().getRegisterDeRetour());
        compiler.addComment("--------EndBinaryOp--------"+getLocation()+"-----");
    }

    /**
     * Cette methode v??rifie si on travail avec des floats, elle sera utilis??e dans 
     * la division et les operation artithmetiques
     * @param compiler
     */
    public void checkIfWeWorkWithFloatAndIfConvIsNeeded(DecacCompiler compiler){
        LOG.debug("[AbstractBinaryExpr][checkIfWeWorkWithFloatAndIfConvIsNeeded] Checking if i am working with floats");
        if (getLeftOperand().getType() == compiler.environmentType.FLOAT 
            && getRightOperand().getType() == compiler.environmentType.FLOAT ){
            workWithFloats = true;
        }
        if (getLeftOperand().getType() == compiler.environmentType.INT 
            && getRightOperand().getType() == compiler.environmentType.FLOAT ){
            workWithFloats = true;
        }
        if (getLeftOperand().getType() == compiler.environmentType.FLOAT 
            && getRightOperand().getType() == compiler.environmentType.INT ){
            workWithFloats = true;
        }
        if (getLeftOperand().getType() == compiler.environmentType.INT 
            && getRightOperand().getType() == compiler.environmentType.INT ){
            workWithFloats = false;
        }
    }

    /** 
     * Cette m??thode est utilis??e par les classes qui h??rite cette m??thodes afin de pouvoir s'ajuster 
     * au different type d'operation binaire (par exemple addition, )
     * @param compiler
     * @param val
     * @param resultRegister
     */
    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister) throws CodeGenError {
        throw new CodeGenError(getLocation(), "This method should not be visited");
    }

    private DAddr rightOperandIdentifier(DecacCompiler compiler,AbstractExpr expr) throws CodeGenError {
        /*
         * Si on travaille avec l'operation arithm??tique divide avec des entiers il faut transformer
         * l'identificateur de gauche en un registre afin de pourvoir le compar?? avec 0
         * car on n'a ne peux pas compar?? une adresse avec literal
         */
        if ( !(this instanceof Divide && !workWithFloats) && expr instanceof Identifier && !((Identifier)expr).getExpDefinition().isField()) {
            return ((Identifier) expr).getExpDefinition().getOperand();
        }
        LOG.debug("[AbstractBinaryExpr][codeGenInst] Exploring Right");           
        getRightOperand().codeGenInst(compiler);
        LOG.debug("[AbstractBinaryExpr][rightOperandIdentifier] Right operation is placed in the following register  " +
                         getRightOperand().getRegisterDeRetour());
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
