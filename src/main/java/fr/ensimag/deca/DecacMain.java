package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("Deca 1.0 Compiler\n\n@author gl15\n\nCopyright 2023- Free Software Foundation, Ensimag");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            System.out.println("Please place the files you want to compile in the arguments of the program\n");
            options.displayUsage();
            System.exit(0);
        }
        if (options.getParallel()) {
            int numberCore = Runtime.getRuntime().availableProcessors();
            ExecutorService threadPool = Executors.newFixedThreadPool(numberCore); //8 threads because i's the number of cores 
            Future<?> f = null;
            List<Future<?>> listTaches = new ArrayList<>();
            for (int i=0;i<options.getSourceFiles().size(); ++i){
                MultiThread tache = new MultiThread(options, options.getSourceFiles().get(i));
                f = threadPool.submit(tache);
                listTaches.add(f);
            }
            //traversal the list to make sure all the  multiple jobs are finished
            for (int i=0;i<listTaches.size(); ++i){
                listTaches.get(i).get();
            }
            threadPool.shutdown();
/*             for (int i=0;i<options.getSourceFiles().size(); ++i){
                MultiThread m = new MultiThread(options, options.getSourceFiles().get(i));
                Thread t = new Thread(m);
                t.start();
                if (m.isError()){
                    error = true;
                }
            } */
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
