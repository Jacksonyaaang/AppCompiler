package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;

import javax.print.DocFlavor.STRING;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Definition of a method
 *
 * @author gl15
 * @date 01/01/2023
 */
public class MethodDefinition extends ExpDefinition {

    private static final Logger LOG = Logger.getLogger(MethodDefinition.class);

    
    @Override
    public boolean isMethod() {
        return true;
    }

    public Label getLabel() {
        if (label == null){
            LOG.debug("[MethodDefinition][getLabel] Label is null");
        }
        Validate.isTrue(label != null,
                "setLabel() should have been called before");
        return label;
    }

    public void setLabel(Label label) {
        if (this.label == null){
            LOG.debug("[MethodDefinition][setLabel] Initialising label value ");
            this.label = label;
        }
        LOG.debug("[MethodDefinition][setLabel] Trying to modify label with " + label.toString());
    }

    public int getIndex() {
        return index;
    }

    private int index;

    @Override
    public MethodDefinition asMethodDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    private final Signature signature;
    private Label label = null;
    
    /**
     * 
     * @param type Return type of the method
     * @param location Location of the declaration of the method
     * @param signature List of arguments of the method
     * @param index Index of the method in the class. Starts from 0.
     */
    public MethodDefinition(Type type, Location location, Signature signature, int index) {
        super(type, location);
        this.signature = signature;
        this.index = index;
    }

    public Signature getSignature() {
        return signature;
    }

    @Override
    public String getNature() {
        return "method";
    }

    private String methodName = null;

    public String setMethodname(String name) {
        return methodName = name;
    }

    public String getMethodname() {
        return methodName;
    }

    @Override
    public boolean isExpression() {
        return false;
    }

}
