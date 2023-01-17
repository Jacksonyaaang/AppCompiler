package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl15
 * @date 01/01/2023
 */
public class DeclClass extends AbstractDeclClass {
    protected final AbstractIdentifier name ;
    protected final AbstractIdentifier superClass; 
    protected final ListDeclField fields;
    protected final ListDeclMethod methods;


    public void codeGenTableauDeMethod(DecacCompiler compiler) throws CodeGenError {
        ClassDefinition classDefinition = name.getClassDefinition();
        // LEA 1 (GB), R0 
        ClassDefinition classDefinitionSuper = superClass.getClassDefinition();
        //On met l'adresse du debut de la table de méthode du parent dans le registre R0
        compiler.addInstruction(new LEA( compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(classDefinitionSuper),
                                         Register.getR(0)));
        compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().put(classDefinition, new RegisterOffset(compiler.incrementGbCompiler(), Register.GB));
        compiler.addInstruction(new STORE(Register.getR(0), compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().get(classDefinition)));
        MethodDefinition methodDefinitionIter;
        ClassDefinition currentMethodDefinition;
        for (int methodIndex = 1; methodIndex <= classDefinition.getNumberOfMethods(); methodIndex++){
            //On recupére la premier méthode avec l'index qui égal à methodIndex, ensuite on cherche la classe
            //auquel cette méthode appartient 
            methodDefinitionIter = classDefinition.getMembers().getMethodIndex(methodIndex);
            currentMethodDefinition = getClassdefinition(compiler, classDefinition.getMembers().getMethodEnvioremnent(
                                        methodDefinitionIter));
            compiler.addInstruction(new LOAD((DVal) new LabelOperand(new Label("code."+
                                                            currentMethodDefinition.getType().getName().getName()+"."+
                                                            methodDefinitionIter.getType().getName().getName())),
                                                            Register.getR(0)));
            compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(compiler.incrementGbCompiler(), Register.GB)));
        }
    }
 
    public ClassDefinition getClassdefinition(DecacCompiler compiler, EnvironmentExp envExpr){
        for(ClassDefinition classDefinitionIter: compiler.getTableDeMethodeCompiler().getAdresseTableDeMethod().keySet()){
            if (classDefinitionIter.getMembers() == envExpr){
                return classDefinitionIter;
            }
        }
        return null;
    }



    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass, ListDeclField fields, ListDeclMethod methods) {
        Validate.notNull(name);
        Validate.notNull(superClass);
        Validate.notNull(fields);
        Validate.notNull(methods);
        this.name = name ;
        this.superClass = superClass; 
        this.fields = fields;
        this.methods = methods;
    }

    public ListDeclField getFields(){
        return fields;
    }
    public ListDeclMethod getMethods() {
        return methods;
    }
    public AbstractIdentifier getName() {
        return name;
    }
    public AbstractIdentifier getSuperClass() {
        return superClass;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        s.print(getName().getName().getName());
        if(getSuperClass() != null && getSuperClass().getName().getName()!= "object"){
            s.print(" extends ");
            s.print(getSuperClass().getName().getName());
        }
        s.println(" {");
        s.indent();
        getFields().decompile(s);
        getMethods().decompile(s);
        s.unindent();
        s.println("}");
    }



    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        if (!compiler.environmentType.getEnvTypes().containsKey(name.getName())){
            if (compiler.environmentType.getEnvTypes().containsKey(superClass.getName())){
                //Ce traitement associe à l'indetificateur object, la class definition,
                // ceci sera utile pour le méthode de generation de code 
                if (superClass.getName() == compiler.createSymbol("object")){
                     superClass.setDefinition(compiler.environmentType.getEnvTypes().get(
                                                compiler.createSymbol("object")));
                }
                ClassType classType = new ClassType(name.getName(), getLocation(), 
                                                    (ClassDefinition)compiler.environmentType.defOfType(superClass.getName()));
                compiler.environmentType.getEnvTypes().put(name.getName(), (ClassDefinition) classType.getDefinition());
            }else
                throw new ContextualError("le super Class n'est pas déclaré", getLocation());
            
        }else 
            throw new ContextualError("Double Déclaration de la class  " + name.getName().getName(), getLocation());
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
               ClassDefinition classDefi = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
               classDefi.setNumberOfFields(classDefi.getSuperClass().getNumberOfFields());
               classDefi.setNumberOfMethods(classDefi.getSuperClass().getNumberOfMethods());
               fields.verifyListDeclField(compiler, classDefi.getMembers(), classDefi);
               methods.verifyListDeclMethod(compiler, classDefi.getMembers(), classDefi);
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition classDefi = (ClassDefinition) compiler.environmentType.defOfType(name.getName());
        fields.verifyInitFields(compiler, classDefi.getMembers(), classDefi);
        methods.verifyListDeclMethodBody(compiler, classDefi.getMembers(), classDefi);
        //throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false); 
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f) ;
        superClass.iter(f); 
        fields.iter(f);
        methods.iter(f);
    }

}
