package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Selection extends AbstractLValue {

    protected AbstractExpr obj;
    protected AbstractIdentifier field;

    public Selection(AbstractExpr obj, AbstractIdentifier field){
        Validate.notNull(obj);
        Validate.notNull(field);
        this.obj = obj;
        this.field = field;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError {
        obj.codeGenInst(compiler);
        if (!compiler.getCompilerOptions().isNoCheck()){
            compiler.addInstruction(new CMP(new NullOperand(), obj.getRegisterDeRetour()), null);
            compiler.addInstruction(new BEQ(new Label("deref_null_error")), 
                                    "Checking if the class identifier is null");
            compiler.getErrorManagementUnit().activeError("deref_null_error");
        }
        compiler.addInstruction(new LOAD(
                        new RegisterOffset( ((Identifier)field).getFieldDefinition().getIndex(), obj.getRegisterDeRetour()), obj.getRegisterDeRetour()),
                         "Loading the field " + field.getName() +" into a register "); 
        this.setRegisterDeRetour(obj.getRegisterDeRetour());
        this.transferPopRegisters(obj.getRegisterToPop());
    }



    public AbstractIdentifier getField() {return field;}
    public AbstractExpr getObj() {return obj;}

    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // TODO Auto-generated method stub
        return compiler.environmentType.INT;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getObj().decompile(s);
        s.print(".");
        getField().decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);       
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);           
        field.iter(f) ;
    }
    
}
