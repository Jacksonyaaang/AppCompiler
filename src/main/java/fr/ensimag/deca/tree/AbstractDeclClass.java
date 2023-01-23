package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CodeGenError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.IMAProgram;

/**
 * Class declaration.
 *
 * @author gl15
 * @date 01/01/2023
 */
public abstract class AbstractDeclClass extends Tree {

    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
     * without looking at its content.
     */
    protected abstract void verifyClass(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
     */
    protected abstract void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
     */
    protected abstract void verifyClassBody(DecacCompiler compiler)
            throws ContextualError;

     /**
      * Cette méthode est utilisée pour génenerer la tableau de méthode d'une classe spécfique
      Cette méthode est uniquement invoquée dans le pass 1 de la géneration du code 
      * @param compiler 
      * @throws CodeGenError cette erreur est interne aux compilateur et elle est lancée 
      quand le compilateur génere une erreur dans les algorithmes de création de code assembleur 
      */
     protected abstract void codeGenTableauDeMethod(DecacCompiler compiler) throws CodeGenError;


     /**
      * Cette méthode est utilisée pour génenerer le codes de tout les methodes d'une fonctions et la 
      * sa initialisation.
      * @param compiler 
      * @throws CodeGenError cette erreur est interne aux compilateur et elle est lancée 
      quand le compilateur génere une erreur dans les algorithmes de création de code assembleur 
      */
      protected abstract void codeGenClassMethod(DecacCompiler compiler) throws CodeGenError;


      /**
       * getClassProgram retourne le programme de classe qui contient la méthode d'initialisation
       * et les methodes de la classe
       * @return le program de la classe
       */  
      public IMAProgram getClassProgram() {
        return null;
        }
}
