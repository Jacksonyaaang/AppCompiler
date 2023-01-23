package fr.ensimag.deca.tree;

import java.io.PrintStream;

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
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;


public class InstanceOf extends AbstractExpr {

    private static final Logger LOG = Logger.getLogger(DeclClass.class);
    
    protected AbstractExpr expr;
    protected AbstractIdentifier typeInstance;

    public AbstractExpr getExpr() {
        return expr;
    }

    public AbstractIdentifier getTypeInstance() {
        return typeInstance;
    }

    public InstanceOf(AbstractExpr expr, AbstractIdentifier typeInstance){
        Validate.notNull(expr);
        Validate.notNull(typeInstance);
        this.expr = expr;
        this.typeInstance = typeInstance;
    }

    protected void codeGenInst(DecacCompiler compiler) throws CodeGenError{
        compiler.addComment("--------------BeginInstanceof----------"+getLocation()+"+-----");
        LOG.debug("[DeclClass][codeGenTableauDeMethod] Current methode table adresses \n" + compiler.getTableDeMethodeCompiler().toString()); 
        compiler.incrementInstanceOfIncrementer();
        GPRegister regStableClassType=Register.getR(0);
        //this.LoadGencode(compiler, false);;
        Label loopbegin = new Label("loopbegin"+compiler.getInstanceOfIncrementer());
        Label endtrue = new Label("endtrue"+compiler.getInstanceOfIncrementer());
        Label endfalse = new Label("endfalse"+compiler.getInstanceOfIncrementer());
        Label instanceOfObject = new Label("instanceOf_Object"+compiler.getInstanceOfIncrementer());
        
        if (expr.getType().isNull()){
            compiler.addInstruction(new BRA(instanceOfObject));
        }

        //compiler.addInstruction(new WSTR("START instanceof"));
        //compiler.addInstruction(new WNL());
        /**
         * On load la class fixe
         */
        compiler.addInstruction(new LEA(compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(typeInstance.getClassDefinition()), 
                        regStableClassType), "loading method table of " + typeInstance.getName());
        /**
         * On load la classe variable, on compare initialiment s'il s'agit de la même adresse
         */
        compiler.addInstruction(new LEA(compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(compiler.environmentType.getEnvTypes().get(expr.getType().getName())), 
        Register.getR(1)), "loading method table of " + expr.getType().getName());
        compiler.addInstruction(new CMP(Register.getR(1), regStableClassType));
        compiler.addInstruction(new BEQ(instanceOfObject), "si les deux classes sont les mêmes , on retourne immédiatement true");

        this.expr.codeGenInst(compiler);
        GPRegister regLoop = expr.getRegisterDeRetour();

        //compiler.addInstruction(new WSTR("BEFORE LOOP"));
        //compiler.addInstruction(new WNL());
        
        compiler.addLabel(loopbegin);  
        //compiler.addInstruction(new WSTR("INSIDE LOOP"));
        //compiler.addInstruction(new WNL());
        compiler.addInstruction(new CMP(regStableClassType, regLoop));
        compiler.addInstruction(new BEQ(instanceOfObject), "loopend");
        compiler.addInstruction(new LOAD(new RegisterOffset(0, regLoop), regLoop));
        compiler.addInstruction(new CMP(new NullOperand(), regLoop));
        compiler.addInstruction(new BEQ(endfalse));
        compiler.addInstruction(new BRA(loopbegin));

        compiler.addLabel(instanceOfObject);
        //compiler.addInstruction(new WSTR("TRUE "));
        //compiler.addInstruction(new WNL());
        compiler.addInstruction(new LOAD(1, regStableClassType));
        compiler.addInstruction(new BRA(endtrue));
        
        compiler.addLabel(endfalse);
        //compiler.addInstruction(new WSTR("FALSE "));
        //compiler.addInstruction(new WNL());
        compiler.addInstruction(new LOAD(0, regStableClassType));
        
        compiler.addLabel(endtrue); 
        compiler.addInstruction(new LOAD(regStableClassType, regLoop));
        //compiler.addInstruction(new WSTR("END instanceof"));
        //compiler.addInstruction(new WNL());

        // expr.popRegisters(compiler);
        // compiler.getRegisterManagement().decrementOccupationRegister(expr.getRegisterDeRetour());
        this.setRegisterDeRetour(regLoop);
        this.transferPopRegisters(expr.getRegisterToPop());
        //On n'a pas besoin de transporter les registre à poper car, on reserve le registre dans cette classe
        compiler.addComment("--------------EndInstanceof----------"+getLocation()+"-----");    
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t1 = expr.verifyExpr(compiler, localEnv, currentClass);
        expr.setType(t1);
        if (!t1.isClassOrNull()){
            throw new ContextualError("instanceof doit être avec des classes", getLocation());
        }
        Type t2 = typeInstance.verifyType(compiler);
        typeInstance.setType(t2);
        if (!t2.isClass() || t2.isNull()){
            throw new ContextualError("L' identificateur à droite de instanceof doit être une classe non null", getLocation());
        }
        return compiler.environmentType.BOOLEAN;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getExpr().decompile(s);
        s.print(" instanceof ");
        getTypeInstance().decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);       
        typeInstance.prettyPrint(s, prefix, true);    
    }      

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);           
        typeInstance.iter(f) ;
    }
    
}
