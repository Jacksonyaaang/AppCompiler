package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class MultiThread implements Runnable{

    private CompilerOptions compilerOptions;
    private File fichier;
    private boolean error=false;
    

    public boolean isError() {
        return error;
    }


    public MultiThread(CompilerOptions compilerOptions, File fichier) {
        this.compilerOptions = compilerOptions;
        this.fichier = fichier;
    }


    @Override
    public void run() {
        Logger logger = Logger.getRootLogger();
        logger.info("Starting thread to compile " + compilerOptions.getSourceFiles().get(0));
        DecacCompiler com = new DecacCompiler(compilerOptions, fichier);
        if (com.compile()) {
            error = true;
        }
    }
    
}


