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
    
    private HashSet<String> optionPool = new HashSet<>();  //hash set pour stocker les options 


    private void setUpOptions(){
        //On ajoute tout le options possibles
        optionPool.add("-b");
        optionPool.add("-p");
        optionPool.add("-v");
        optionPool.add("-n");
        optionPool.add("-r");  //-r X
        optionPool.add("-d");
        optionPool.add("-P");
        optionPool.add("-w");
    }

    //Ce tableau de boolean indique si une option a ete deja appelee 
    //les options sont organisees de la maniere suivante
    // (par ordre : p->0, v->1, n->2 , r->3, d->4, P->5, w->6)
    private boolean[] optionsInvoked;
    int numberOfOptions = 7; //besides -b
    
    private boolean decompile;
    private boolean verfiryAndStop;
    /**
     * Si la valeur de nocheck est vrai, 
     * on n'ajoute pas le traitement d'erreur en runtime
     */
    private boolean noCheck = false;

    
    public boolean isNoCheck() {
        return noCheck;
    }

    /**
     * Cette fonction cree le tableau options Invoked et initiale tout les 
     * valeurs du tableau à 0
     */
    private void initialiseInvokedTable() {
        optionsInvoked  = new boolean[numberOfOptions];
        for (int i=0; i<numberOfOptions; i++){optionsInvoked[i] = false;}
    }
    
    private void printbool() {
        for (int i=0; i<numberOfOptions; i++){
            //System.out.println("bool " + i + " " + optionsInvoked[i]);
        }
    }

    public boolean isDecompile() {
        return decompile;
    }

    public boolean isVerfiryAndStop() {
        return verfiryAndStop;
    }

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

    public int getNumberOfRegisters() {
        return numberOfRegisters;
    }
    
    public void setNumberOfRegisters(int numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
    }


    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    //On specifie le nombre de registres qu'on utilisera
    //La valeur par defaut est egal a 16, mais peut varie entre 4 et 16
    private int numberOfRegisters = 16; 

    /**
     * 
     * @param option
     * @param addStr
     * @throws CLIException Cette exception est jete dans le cas
     *  ou il existe un probleme avec les options donnees
     *  Par exemple : si on a deux options qui sont incompatibles
     */
    private void switchOptionCase(String option, String addStr) throws CLIException
    {
       // (par ordre : p->0, v->1, n->2 , r->3, d->4, P->5, w->6)
        if (optionPool.contains(option)){
            switch(option){
                case "-p":
                    //On verifie si l'option a ete deja detectee, on ne fait rien dans ce cas
                    if (optionsInvoked[0] == true){ break; }
                    if (optionsInvoked[1] == true){ 
                        //Les deux options -v -p sont incompatible
                        throw new CLIException("[Please Retry]\n[Fault Detected!!]Can't pass simultaneously the option '-p' and '-v' ");
                    }
                    optionsInvoked[0]=true;
                    decompile = true;
                    break;
                case "-v":
                    if (optionsInvoked[1]==true){ break; }
                    if (optionsInvoked[0] == true){ 
                        //Les deux options -v -p sont incompatible
                        throw new CLIException("[Please Retry]\n[Fault Detected!!]Can't pass simultaneously the option '-p' and '-v' ");
                    }
                    optionsInvoked[1]=true;
                    verfiryAndStop = true;
                    break;
                case "-n":
                    if (optionsInvoked[2]==true){break;}
                    optionsInvoked[2]=true;
                    noCheck = true;
                    break;
                case "-r":
                    if (optionsInvoked[3]==true){break;}
                    optionsInvoked[3]=true;
                    numberOfRegisters = Integer.parseInt(addStr);
                    break;
                case "-d":
                    if (optionsInvoked[4]==false){debug=1;}
                    if (debug == 3){break;}
                    debug++;
                    break;
                case "-P":   
                    if (optionsInvoked[5]==true){break;}
                    optionsInvoked[5]=true;
                    parallel = true;
                    break;
                case "-w":   
                    if (optionsInvoked[6]==true){break;}
                    optionsInvoked[6]=true;
                    //A FAIRE Affichage de warning de compilation voir poly
                    break;
            }

        }
        else  {
            //Si l'argument donnee n'est pas une option on verifie s'il s'agit d'un fichier 
            //Sinon on jete une exception
            StringBuilder sbFileName = new StringBuilder(option);
            if (sbFileName.substring(sbFileName.length()-5).equals(".deca")){
                sourceFiles.add(new File(sbFileName.toString()));
            }
            else{
                throw new CLIException("[Please Retry]\n[Fault Detected!] Didn't not understand the following paramater: " + option);
            }
        }
    }

    
    public void parseArgs(String[] args) throws CLIException, NumberFormatException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        //when the argument space is empty
        if (args.length==0){throw new CLIException("[Please Retry]\n[Fault Detected!!] Please provide arguments ");    }
        //On initialize les structures de donnees qu'on va utiliser
        setUpOptions();
        initialiseInvokedTable();
        int len = args.length;
        if (args.length==1&&args[0].equals("-b")){
            printBanner=true;
        }
        else{
            for (int i=0;i<len; ++i){
                if (args[i].equals("-b")){
                        throw new CLIException("[Please Retry]\n[Fault Detected!!] Can't use '-b' with multiples arguments");
                }             
                else{
                    if (args[i].equals("-r")){
                        //try catch pour traiter le cas : apres "-r", X est pas une value 
                        try {
                            if (i+1==len||(Integer.parseInt(args[i+1])<4 || Integer.parseInt(args[i+1])>16)){
                                throw new NumberFormatException("[Please Retry]\n[Fault Detected!!] Must have a banalise register value with the range '4<=X<=16' after '-r'");
                            }   
                        } catch (Exception e) { 
                            if (optionPool.contains(args[i+1])){
                                throw new CLIException("[Please Retry]\n[Fault Detected!!] The X after '-r' must with the range '4<=X<=16' VALUE ! ");
                            }else{
                                throw new CLIException("[Please Retry]\n[Fault Detected!!] The X after '-r' must with the range '4<=X<=16' VALUE !");
                            }
                        }
                        switchOptionCase(args[i], args[i+1]); //we extract the number of registers from args[i+1] 
                        i++;
                        continue;
                    }
                    switchOptionCase(args[i], "-1");  
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
    }

    protected void displayUsage() {
        System.out.println("The available options:\n-b\t: affiche une bannière dont le nom de l'équipe\n"+
        "-p\t: arrête decac après l'étape de construction del'arbre, et affiche la décompilation de ce dernier\n"+
        "-v\t: arrête decac après l'étape de vérifications\n"+
        "-n\t: supprime les tests à l'exécution spécifiés dans les points 11.1 et 11.3 de la sémantique de Deca.\n"+
        "-r X\t: limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16\n"+
        "-d\t: active les traces de debug. Répéter l'option plusieurs fois pour avoir plus de traces.\n"+
        "-P\t: s'il y a plusieurs fichiers sources, lance la compilation des fichiers en parallèle (pour accélérer la compilation)\n"+
        " \nusage :'decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]' ");
    }
}
