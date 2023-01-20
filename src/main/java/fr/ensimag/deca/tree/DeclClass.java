package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

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
                ClassType classType = new ClassType(name.getName(), getLocation(), 
                                                    (ClassDefinition)compiler.environmentType.defOfType(superClass.getName()));
                compiler.environmentType.getEnvTypes().put(name.getName(), classType.getDefinition());
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
