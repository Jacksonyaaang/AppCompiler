package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
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
<<<<<<< HEAD
            error = true;
        }
        if (options.getSourceFiles().isEmpty()) {
            System.out.println("Please place the files you want to compile in the arguments of the program");
            error = true;
=======
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            System.out.println("Please place the files you want to compile in the arguments of the program");
            options.displayUsage();
            System.exit(0);
>>>>>>> master
        }
        if (options.getParallel()) {
            for (int i=0;i<options.getSourceFiles().size(); ++i){
                MultiThread m = new MultiThread(options, options.getSourceFiles().get(i));
                Thread t = new Thread(m);
                t.start();
                if (m.isError()){
                    error = true;
                }
            }
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
