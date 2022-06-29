package formula;

import java.math.BigInteger;

public class Sin extends Triangle {
    public Sin(BigInteger outIndex, BigInteger inIndex, BigInteger constant) {
        super(outIndex, inIndex, constant);
    }

    @Override
    public String toString() {
        return "sin" + super.toString();
    }
}
