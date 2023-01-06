package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    
    private HashSet<String> optionPool = new HashSet<>();  //hash table pour stocker les options 
    boolean pyAllIn=false;//pour vérifier p y condition
    boolean[] checkTable = new boolean[6];  //y compris (par ordre : p->0, v->1, n->2 , r->3, P->4, w->5)-> length =6
    
    public int getDebug() {
        return debug;
    }
    
    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }
    
    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    
    
    private void switchOptionCase(String option, String addStr) throws IllegalArgumentException
    {
       // (par ordre : p->0, v->1, n->2 , r->3, P->4, w->5)-
        if (optionPool.contains(option)){
            switch(option){
                case "-p":
                    if (checkTable[0]==true){
                        System.out.println("quit this turn");  //delete after .....................................
                        break;
                    }
                    if (pyAllIn==true){
                        try {
                            throw new IllegalArgumentException();
                        } catch (IllegalArgumentException e) {
                            System.out.println("\n[Please Retry]\n[Fault Detected!!]Can't pass simultaneously the option '-p' and '-v' ");
                            System.exit(1);  //or return ???
                        }
                    }
                    checkTable[0]=true;
                    pyAllIn=true;
                    //pas fini, attendre 'étape de construction d'arbre' est fait
                    System.out.println("arret apres la construction d'arbre........");
                    break;
                case "-v":
                    if (checkTable[1]==true){
                        System.out.println("quit this turn");  //delete after .....................................
                        break;
                    }
                    if (pyAllIn==true){
                        try {
                            throw new IllegalArgumentException();
                        } catch (IllegalArgumentException e) {
                            System.out.println("\n[Please Retry]\n[Fault Detected!!]Can't pass simultaneously the option '-p' and '-v' ");
                            System.exit(1);  //or return ???
                        }
                    }
                    checkTable[1]=true;
                    //throw new IllegalArgumentException("\n[Please Retry]\n[Fault Detected!!]Can't pass simultaneously the option '-p' and '-v' ");
                    pyAllIn=true;
                    //pas fini, att "arrête decac après l'étape de vérifications" est fait
                    System.out.println("arrête decac après l'étape de vérifications");
                    break;
                case "-n":
                    if (checkTable[2]==true){
                        System.out.println("quit this turn");  //delete after .....................................
                        break;
                    }
                    checkTable[2]=true;
                    System.out.println("supprime les tests à l'exécution spécifiés dans..............");
                    break;
                case "-r":
                    if (checkTable[3]==true){
                        System.out.println("quit this turn");  //delete after .....................................
                        break;
                    }
                    checkTable[3]=true;
                    System.out.println("\n working in the -r, with bana.. value is : "+addStr);
                    break;
                case "-d":
                    System.out.println("active the debug info ");
                    break;
                case "-P":  //use the addStr to determinate the name of file 
                    if (checkTable[4]==true){
                        System.out.println("quit this turn");  //delete after .....................................
                        break;
                    }
                    checkTable[4]=true;
                    
                    
            }

        }else
            throw new IllegalArgumentException(option+"is not an available option");


    }

    //-b
    private void bannerOption(){
        System.out.println("Deca 1.0 Compiler\n\n@author gl15\n\nCopyright 2023- Free Software Foundation, Ensimag");
    } 

    private void setUpOptions(){
        optionPool.add("-b");
        optionPool.add("-p");
        optionPool.add("-v");
        optionPool.add("-n");
        optionPool.add("-r");  //-r X
        optionPool.add("-d");
        optionPool.add("-P");
        optionPool.add("-w");
    }
    
    public void parseArgs(String[] args) throws CLIException, NumberFormatException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        //when the argument space is empty
        if (args.length==0){
            System.out.println("The available options:\n-b\t: affiche une bannière dont le nom de l'équipe\n"+
            "-p\t: arrête decac après l'étape de construction del'arbre, et affiche la décompilation de ce dernier\n"+
            "-v\t: arrête decac après l'étape de vérifications\n"+
            "-n\t: supprime les tests à l'exécution spécifiés dans les points 11.1 et 11.3 de la sémantique de Deca.\n"+
            "-r X\t: limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16\n"+
            "-d\t: active les traces de debug. Répéter l'option plusieurs fois pour avoir plus de traces.\n"+
            "-P\t: s'il y a plusieurs fichiers sources, lance la compilation des fichiers en parallèle (pour accélérer la compilation)\n"+
            " \nusage :'decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]' ");
        }
        setUpOptions();
        //a boolean table for eliminating the case duplication ex: decac -r 6 -r -5  : is not acceptable, so neglige the second one 
        int len = args.length;
        if (args.length==1&&args[0].equals("-b"))
            bannerOption();
        else{
            for (int i=0;i<len; ++i){


                if (args[i].equals("-b")){
                    try{
                        throw new IllegalArgumentException();
                    }catch (IllegalArgumentException e){
                        System.out.println("\n[Please Retry]\n[Fault Detected!!] Can't use '-b' with multiples arguments");
                        System.exit(1);  //or return ???
                    }
                }             
                else{
                    if (args[i].equals("-r")){
                        //try catch pour traiter le cas : apres "-r", X est pas une value 
                        try {
                            if (i+1==len||(Integer.parseInt(args[i+1])<=4&&Integer.parseInt(args[i+1])>=16)){
                                try {
                                    throw new IllegalArgumentException();
                                    
                                } catch (IllegalArgumentException e) {
                                    System.out.println("\n[Please Retry]\n[Fault Detected!!] Must have a banalise register value with the range '4<=X<=16' after '-r'");
                                    System.exit(1);  //or return ???
                                }
                            }
                        } catch (NumberFormatException e) {
                            if (optionPool.contains(args[i+1])){
                                //la condition ici pour dire user faut ajouter une bonne valeur apres le -r et  avant la option prochaine
                                System.out.println("\n[Please Retry]\n[Fault Detected!!] Must have a banalise register VALUE with the range '4<=X<=16' after '-r'");
                                System.exit(1);  //or return ???
                            }
                            else  //ici exception pour les cas user a entré n'importe quoi apres le -r 
                                System.out.println("\n[Please Retry]\n[Fault Detected!!] The X after '-r' must with the range '4<=X<=16' VALUE ! ");
                                
                        }
                        switchOptionCase(args[i], args[i+1]);
                        i++;
                        continue;
                    }
                    else if (args[i].equals("-P")){
                        StringBuilder sb= new StringBuilder();
                        continue;

                    }
                    switchOptionCase(args[i], "-1");  //normal case

                }

            }
        } 

    	
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");
    }

    protected void displayUsage() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
