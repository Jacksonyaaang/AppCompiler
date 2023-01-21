/**
 * Operand representing a register indirection with offset and relative movement
 * , e.g. 42(R3, R4). which will lead to the adresse R3+R4+42
 *
 * @author Ensimag
 * @date 01/01/2023
 */
public class RegisterIndirectOffset extends DAddr {
    public int getOffset() {
        return offset;
    }
    public Register getAdressRegister() {
        return adressRegister;
    }
    public Register getRegister() {
        return register;
    }
    private final int offset;
    private final Register register;
    private final Register adressRegister;


    public RegisterIndirectOffset(int offset, Register register, Register adressRegister) {
        super();
        this.offset = offset;
        this.register = register;
        this.adressRegister = adressRegister;
    }

    @Override
    public String toString() {
        return offset + "(" + adressRegister + ", " + register + ")";
    }
}

