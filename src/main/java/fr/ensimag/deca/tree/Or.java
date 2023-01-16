package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Or extends AbstractOpBool {

    private static final Logger LOG = Logger.getLogger(And.class);

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{    
        super.setBoolOpIdentifier(compiler.getStackManagement().incrementOpBoolIncrementer());
        LOG.debug("[Or][codeGenInst] Working on Or boolean operation // Location " +getLocation());
        LOG.debug("[Or][codeGenInst] Exploring Left");
        //A FAIRE, optimisation du code de la boucle if
        compiler.addComment("--------StartOr--------"+getLocation()+"-----");
        getRightOperand().printPopRegisters(compiler);
        getLeftOperand().codeGenInst(compiler);
        LOG.debug("[Or][codeGenInst] Left register " + getLeftOperand().getRegisterDeRetour());
        compiler.addInstruction(new CMP(new ImmediateInteger(1), getLeftOperand().getRegisterDeRetour()), 
                                        "[Or]Comparing in the left branch ");
        compiler.addInstruction(new BEQ(new Label("End_Or_Id_" +super.getBoolOpIdentifier() )), 
                                        "[Or] checking if the first element is false");
        getRightOperand().codeGenInst(compiler);
        if (getRightOperand().getRegisterDeRetour() != getLeftOperand().getRegisterDeRetour()){
            LOG.debug("[Or][codeGenInst] Exploring Right, : " +getLocation());
            compiler.addInstruction(new CMP(new ImmediateInteger(0), getRightOperand().getRegisterDeRetour()), 
                                    "[Or]Comparing in the right branch ");

            compiler.addInstruction(new BEQ(new Label("End_Or_Id_" +super.getBoolOpIdentifier() )), 
                                    "[OR] checking if the second element is true ");

            compiler.addInstruction(new LOAD(new ImmediateInteger(1), getLeftOperand().getRegisterDeRetour()), 
                                    "Or is true , We place the value 1 in the return Register");
            getRightOperand().popRegisters(compiler);
            compiler.addLabel(new Label("End_Or_Id_" +super.getBoolOpIdentifier()));
        }
        else{
            throw new CodeGenError(getLocation(), "Should never have equal registers with this approch; this must never be called");
        }
        this.setRegisterDeRetour(getLeftOperand().getRegisterDeRetour());
        getRightOperand().printPopRegisters(compiler);
        this.transferPopRegisters(getLeftOperand().getRegisterToPop());
        compiler.getRegisterManagement().decrementOccupationRegister(getRightOperand().getRegisterDeRetour());
        compiler.addComment("--------EndOr--------"+getLocation()+"-----");
    }

    
    @Override
    protected String getOperatorName() {
        return "||";
    }


}
