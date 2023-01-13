package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl15
 * @date 01/01/2023
 */
public class And extends AbstractOpBool {

    private static final Logger LOG = Logger.getLogger(And.class);

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{ 
        //A FAIRE, optimisation du code de la boucle if   
        super.setBoolOpIdentifier(compiler.getStackManagement().incrementOpBoolIncrementer());
        LOG.debug("[And][codeGenInst] Working on And boolean operation Location ="+ getLocation());
        LOG.debug("[And][codeGenInst] Exploring Left");
        compiler.addComment("--------StartAND--------"+getLocation()+"-----");
        getLeftOperand().codeGenInst(compiler);
        LOG.debug("Left register " + getLeftOperand().getRegisterDeRetour());
        compiler.addInstruction(new CMP(new ImmediateInteger(0), getLeftOperand().getRegisterDeRetour()), 
                                        "[AND]Comparing in the left branch ");
        compiler.addInstruction(new BEQ(new Label("End_And_False_Id_" +super.getBoolOpIdentifier() )), 
                                        "[AND] checking if the first element is false");
        getRightOperand().codeGenInst(compiler);
        if (getRightOperand().getRegisterDeRetour() != getLeftOperand().getRegisterDeRetour()){
            LOG.debug("[And][codeGenInst] Exploring Right");
            compiler.addInstruction(new CMP(new ImmediateInteger(0), getRightOperand().getRegisterDeRetour()), 
                                    "[AND]Comparing in the right branch ");
            compiler.addInstruction(new BNE(new Label("AND_Success_id" +super.getBoolOpIdentifier() )), 
                            "[AND] checking if the first element is true");
            compiler.addLabel(new Label("End_And_False_Id_" +super.getBoolOpIdentifier()));
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), getLeftOperand().getRegisterDeRetour()), "And is false, We place the value 0 in the return Register");
            compiler.addInstruction(new BRA(new Label("QUIT_AND_Called_When_Fail_id" +super.getBoolOpIdentifier())), 
                                    "[AND] Branch will be used in the case when the left operand is false"+
                                    "in order to avoir poping pushing operations relate to right operand");
            compiler.addLabel(new Label("AND_Success_id" +super.getBoolOpIdentifier()));
            getRightOperand().popRegisters(compiler);
            compiler.addLabel(new Label("QUIT_AND_Called_When_Fail_id" +super.getBoolOpIdentifier()));
        }
        else{
            throw new CodeGenError(getLocation(), "Should never have equal registers with this approch; this must never be called");
        }
        this.setRegisterDeRetour(getLeftOperand().getRegisterDeRetour());
        this.transferPopRegisters(getLeftOperand().getRegisterToPop());
        compiler.getRegisterManagement().decrementOccupationRegister(getRightOperand().getRegisterDeRetour());
        compiler.addComment("--------EndAND--------"+getLocation()+"-----");
    }


    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
