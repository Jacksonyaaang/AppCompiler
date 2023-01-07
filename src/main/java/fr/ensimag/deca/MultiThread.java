package fr.ensimag.deca;

import java.io.File;

public class MultiThread implements Runnable{

    private CompilerOptions compiler;
    private File fichier;
    private boolean error=false;
    

    public boolean isError() {
        return error;
    }


    public MultiThread(CompilerOptions compiler, File fichier) {
        this.compiler = compiler;
        this.fichier = fichier;
    }


    @Override
    public void run() {
        DecacCompiler com = new DecacCompiler(compiler, fichier);
        if (com.compile()) {
            error = true;
        }
    }
    
}


