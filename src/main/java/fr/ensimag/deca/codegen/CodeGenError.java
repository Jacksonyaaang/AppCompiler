package fr.ensimag.deca.codegen;

/**
 * Exception raised when a contextual error is found.
 *
 * @author gl15
 * @date 01/01/2023
 */
public class CodeGenError extends Exception {
    boolean[] listError = new boolean[4];
    
    private static final long serialVersionUID = -8122514996569278575L;

    public CodeGenError(String message) {
        super(message);
    }
}
