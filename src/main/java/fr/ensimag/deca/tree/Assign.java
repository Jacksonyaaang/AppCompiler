package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Assigner;
import net.bytebuddy.dynamic.scaffold.inline.AbstractInliningDynamicTypeBuilder;
import fr.ensimag.deca.codegen.CodeGenError;
import org.apache.log4j.Logger;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
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
            LOG.debug("[Assign][codeGenInst]Assign debug " + getLocation()  );    
            GPRegister assignRegister = this.LoadGencode(compiler, false);
            compiler.addComment("--------BeginAssignOp--------"+getLocation()+"-----");    
            this.getRightOperand().codeGenInst(compiler);
            getRightOperand().printPopRegisters(compiler);
            assert( this.getRightOperand().getRegisterDeRetour() != null);

            /*
             * Le pr??mier if traite le cas ou on fait l'assign d'une valeur qui n'est pas un field d'une class
             * et dans ce cas le traitement est basique: on calcule l'operand de droite
             * ensuite on reserve un registre, on stocke la valeur calcul?? dans l'adresse 
             * et enfin on retourne la valeur calcul?? dans le registre reserv??e 
             */
            if (getLeftOperand() instanceof Identifier && !(((Identifier)getLeftOperand()).getExpDefinition().isField())){
                LOG.debug("[Assign][codeGenInst]Left operand is =  " + ((Identifier) getLeftOperand()).getName());    
                LOG.debug("[Assign][codeGenInst]Left is being stored at " + ((Identifier) getLeftOperand()).getExpDefinition().getOperand());
                assert(((Identifier) getLeftOperand()).getExpDefinition().getOperand() != null);
                assert(getLeftOperand() instanceof Identifier);
                LOG.debug("[Assign][codeGenInst] Assiging a value to " + ((Identifier) getLeftOperand()).getName());
                compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),
                                                ((Identifier) getLeftOperand()).getExpDefinition().getOperand()),                                          
                                                " Assiging a value to " + ((Identifier) getLeftOperand()).getName()); 
                compiler.addInstruction(new LOAD(this.getRightOperand().getRegisterDeRetour(), assignRegister),                                          
                                    " Return value of the assignement of ="+ ((Identifier) getLeftOperand()).getName()+ "and storing it into " + assignRegister );
            }
            /*
             * Ce else if traite le cas ou on travail avec field d'une classe qui est acceder directement
             * et ceci ne peut ??tre possible que dans la classe elle m??me 
             */
            else if (getLeftOperand() instanceof Identifier && ((Identifier)getLeftOperand()).getExpDefinition().isField()) {
                //Cette assign ne peut ???tre appel??e quand dans une classe car on peut pas acceder au field directement 
                //sans passer par la section hors de la classe elle m??me
                LOG.debug("[Assign][codeGenInst]Left operand in assign operation is field");
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), assignRegister),
                            "loading parent class =  "+ ((Identifier)getLeftOperand()).getFieldDefinition().getContainingClass().getType().getName() 
                            +" into memory when working with field " + ((Identifier)getLeftOperand()).getName() );
                compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),new RegisterOffset(((((Identifier )getLeftOperand()).getFieldDefinition())).getIndex(), assignRegister)),
                            "Saving field  "+  ((Identifier)getLeftOperand()).getName() + " into memory");

                compiler.addInstruction(new LOAD(this.getRightOperand().getRegisterDeRetour(), assignRegister),                                          
                " Return value of the assignement of ="+ ((Identifier) getLeftOperand()).getName()+ "and storing it into " + assignRegister );
            }
            else if (getLeftOperand() instanceof Identifier && ((Identifier)getLeftOperand()).getExpDefinition().isTable()) {
                //Cette assign ne peut ???tre appel??e quand dans une classe car on peut pas acceder au field directement 
                //sans passer par la section hors de la classe elle m??me
                LOG.debug("[Assign][codeGenInst]Left operand in assign operation is a table");
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), assignRegister),
                            "loading parent class =  "+ ((Identifier)getLeftOperand()).getFieldDefinition().getContainingClass().getType().getName() 
                            +" into memory when working with field " + ((Identifier)getLeftOperand()).getName() );
                compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),new RegisterOffset(((((Identifier )getLeftOperand()).getFieldDefinition())).getIndex(), assignRegister)),
                            "Saving field  "+  ((Identifier)getLeftOperand()).getName() + " into memory");

                compiler.addInstruction(new LOAD(this.getRightOperand().getRegisterDeRetour(), assignRegister),                                          
                " Return value of the assignement of ="+ ((Identifier) getLeftOperand()).getName()+ "and storing it into " + assignRegister );
            }
            /*
             * Ce else if traite le cas ou on travail avec une selection dans l'operand gauche de assign
             * qui peut ??tre de deux types: this, ou une instance de classe ou un tableau
             * dans le cas ou c'est un this, cela est similaire ?? un acc??es ?? un field directement 
             * ?? partie de classe comme le code plus haut
             * Dans le cas d'un tableau, on doit lancer une erreur car les dimensions du tableau ne peuvent pas ??tre chang??
             */
            else if (getLeftOperand() instanceof Selection ){
                if ( ((Selection)getLeftOperand()).getObj() instanceof This){
                    LOG.debug("[Assign][codeGenInst]Left operand in assign operation is this.field");
                    compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), assignRegister),
                                "loading parent class =  "+ ((Identifier)(((Selection)getLeftOperand()).getField())).getFieldDefinition().getContainingClass().getType().getName() 
                                +" into memory when working with field " + ((Identifier)(((Selection)getLeftOperand()).getField())).getName() );
                    
                    compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),new RegisterOffset(((((Identifier)(((Selection)getLeftOperand()).getField())).getFieldDefinition())).getIndex(), assignRegister)),
                                "Saving field  "+  (((Identifier)(((Selection)getLeftOperand()).getField()))).getName() + " into memory");
                    
                    compiler.addInstruction(new LOAD(this.getRightOperand().getRegisterDeRetour(), assignRegister),                                          
                        " Return value of the assignement of this . = "+ (((Identifier)(((Selection)getLeftOperand()).getField()))).getName()+ " and storing it into " + assignRegister );
                }
                else if ( !((Selection)getLeftOperand()).getObj().getType().isTable()) {
                    ((Selection)getLeftOperand()).getObj().codeGenInst(compiler);
                    
                    if (!compiler.getCompilerOptions().isNoCheck()){
                        compiler.addInstruction(new CMP(new NullOperand(), ((Selection)getLeftOperand()).getObj().getRegisterDeRetour()));
                        compiler.addInstruction(new BEQ(new Label("deref_null_error")),
                                                "Checking if the class identifier is null");
                        compiler.getErrorManagementUnit().activeError("deref_null_error");
                    }
                    compiler.addInstruction(new STORE(this.getRightOperand().getRegisterDeRetour(),new RegisterOffset(((((Identifier)(((Selection)getLeftOperand()).getField())).getFieldDefinition())).getIndex(), ((Selection)getLeftOperand()).getObj().getRegisterDeRetour())),
                    "Saving field  "+  (((Identifier)(((Selection)getLeftOperand()).getField()))).getName() + " into memory");
                    ((Selection)getLeftOperand()).getObj().popRegisters(compiler);
                    
                    compiler.getRegisterManagement().decrementOccupationRegister(((Selection)getLeftOperand()).getObj().getRegisterDeRetour());
                    
                    compiler.addInstruction(new LOAD(this.getRightOperand().getRegisterDeRetour(), assignRegister),                                          
                        " Return value of the assignement of ="+ (((Identifier)(((Selection)getLeftOperand()).getField()))).getName()+ "and storing it into " + assignRegister );
                }
                else if (((Selection)getLeftOperand()).getObj().getType().isTable()) {
                    //Les dimensons d'un tableau ne peuvent pas ??tre chang??, on lance une erreur dans ce cas 
                    if (!(compiler.getCompilerOptions().isNoCheck())){
                        compiler.addInstruction(new BRA(new Label("table_dimension_can_not_be_changed")));
                        compiler.getErrorManagementUnit().activeError("table_dimension_can_not_be_changed");
                    }
                }
            }
            else if (getLeftOperand() instanceof TableGetElement ){
                    //On appelle une methode qui fait le calcule de la position d'operateur gauche
                    ( (TableGetElement) getLeftOperand()).saveRegsiterIntoAdress(compiler, getRightOperand());
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
            if ( getLeftOperand() instanceof Identifier && (((Identifier)getLeftOperand()).getExpDefinition().isMethod())) {
                throw new ContextualError("on peut pas assigner une valeur ?? une method", getLocation());
            }
            //Si on n'utilise pas la m??thode readInt ou readFloat lors de l'affectation, on v??rifie l'expression de droite de l'affectation
            if (!(getRightOperand() instanceof AbstractReadExpr)){
                setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, typOpLeft));
                if (getRightOperand() instanceof Identifier && ((Identifier)getRightOperand()).getExpDefinition().isMethod()){
                    throw new ContextualError("on peut pas assigner une methode", getLocation());
                }
            }
            //Si on utilise la m??thode readInt ou readFloat lors de l'affectation, on v??rifie l'expression associ??e
            else{
                Type typOpRight = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                if (!typOpLeft.sameType(typOpRight))
                    throw new ContextualError("Impossible d'assigner le r??sultat de la m??thode read de type " + typOpRight.getName().getName() + " ?? cette variable de type " + typOpLeft.getName().getName(), getLocation());
            }
            setType(typOpLeft);
            return getType();
        }


    @Override
    protected String getOperatorName() {
        return "=";
    }

}
