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
    private HashSet<String> optionPool;  //hash table pour stocker les options 
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
    
    
    private void switchOptionCase(String option)
    {
        boolean pyAllIn = false;


    }

    //-b
    private void bannerOption(){
        System.out.println("Deca 1.0 Compiler\n\n@author gl15\n\nCopyright 2023- Free Software Foundation, Ensimag");
    } 
    
    public void parseArgs(String[] args) throws CLIException {
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
    	optionPool = new HashSet<>();
        optionPool.add("-b");
        optionPool.add("-p");
        optionPool.add("-v");
        optionPool.add("-n");
        optionPool.add("-r");  //-r X
        optionPool.add("-d");
        optionPool.add("-P");
        optionPool.add("-w");
         if (args.length==1&&args[0].equals("-b"))
            bannerOption();
        else{
            for (int i=0;i<args.length; ++i){
                if (args[i].equals("-b"))
                    throw new IllegalArgumentException("can't use '-b' with multiples arguments");
                else
                    switchOptionCase(args[i]);
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
