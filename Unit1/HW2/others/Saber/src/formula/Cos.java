package formula;

import java.math.BigInteger;

public class Cos extends Triangle {
    public Cos(BigInteger outIndex, BigInteger inIndex, BigInteger constant) {
        super(outIndex, inIndex, constant);
    }

    @Override
    public String toString() {
        return "cos" + super.toString();
    }
}
