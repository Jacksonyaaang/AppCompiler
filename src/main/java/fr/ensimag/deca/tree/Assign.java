package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Assigner;
import fr.ensimag.deca.codegen.CodeGenError;
import org.apache.log4j.Logger;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    private static final Logger LOG = Logger.getLogger(Assign.class);

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    public void executeBinaryOperation(DecacCompiler compiler, DVal val, GPRegister resultRegister){
        LOG.debug("[Assign][executeBinaryOperation] generating code for int literal value " );
        LOG.debug("[Assign][executeBinaryOperation] generating code for assignement of: " 
                        + val + " to " + resultRegister);
        compiler.addInstruction(new LOAD(val, resultRegister));
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) throws CodeGenError {
            //Avec deux register il n'y a pas de risque que le compilateur
            //avait fait un push de se registre 
            //On commence par reserver un registre, que sera utilisée pour le retourner la valeur de assign
            GPRegister assignRegister = this.LoadGencode(compiler, false);
            compiler.addComment("--------BeginAssignOp--------"+getLocation()+"-----");    
            this.getRightOperand().codeGenInst(compiler);
            getRightOperand().printPopRegisters(compiler);
            assert( this.getRightOperand().getRegisterDeRetour() != null);
            if (getLeftOperand() instanceof Identifier && !(((Identifier)getLeftOperand()).getExpDefinition().isField())){//&& !((Identifier) getLeftOperand()).){
                LOG.debug("[Assign][codeGenInst]Left operand is =  " + ((Identifier) getLeftOperand()).getName());    
                LOG.debug("[Assign][codeGenInst]Left is being stored at " + ((Identifier) getLeftOperand()).getExpDefinition().getOperand());
                assert(((Identifier) getLeftOperand()).getExpDefinition().getOperand() != null);
                assert(getLeftOperand() instanceof Identifier);
                LOG.debug("[Assign][codeGenInst] Assiging a value to " + ((Identifier) getLeftOperand()).getName());
                compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),
                                                ((Identifier) getLeftOperand()).getExpDefinition().getOperand()),                                          
                                                " Assiging a value to " + ((Identifier) getLeftOperand()).getName()); 
                compiler.addInstruction(new LOAD(this.getRightOperand().getRegisterDeRetour(),assignRegister),                                          
                                    " Return value of the assignement of ="+ ((Identifier) getLeftOperand()).getName()+ "and storing it into " + assignRegister );
            }
            else if (((Identifier)getLeftOperand()).getExpDefinition().isField()) {
                //Cette assgin ne peut ếtre appelée quand dans une classe car on peut pas acceder au field directement 
                //sans passer par la section hors de la classe elle même
                LOG.debug("[Assign][codeGenInst]Left operand in assign operation is field");
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), assignRegister),
                            "loading parent class =  "+ ((Identifier)getLeftOperand()).getFieldDefinition().getContainingClass().getType().getName() 
                            +" into memory when working with field " + ((Identifier)getLeftOperand()).getName() );
                compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),new RegisterOffset(((((Identifier )getLeftOperand()).getFieldDefinition())).getIndex(), assignRegister)),
                            "Saving field  "+  ((Identifier)getLeftOperand()).getName() + " into memory");

            }
            this.setRegisterDeRetour(assignRegister);
            getRightOperand().popRegisters(compiler);
            compiler.getRegisterManagement().decrementOccupationRegister(getRightOperand().getRegisterDeRetour());
            compiler.addComment("--------EndAssignOp--------"+getLocation()+"-----");
        }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("[Assign][verifyExpr] Verify left and right expression in assignment");
        Type typOpLeft = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        //Si on n'utilise pas la méthode readInt ou readFloat lors de l'affectation, on vérifie l'expression de droite de l'affectation
        if (!(getRightOperand() instanceof AbstractReadExpr))
            setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, typOpLeft));
        //Si on utilise la méthode readInt ou readFloat lors de l'affectation, on vérifie l'expression associée
        else{
            Type typOpRight = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            if (!typOpLeft.sameType(typOpRight))
                throw new ContextualError("Impossible d'assigner le résultat de la méthode read à cette variable", getLocation());
        }
        setType(typOpLeft);
        return getType();
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
