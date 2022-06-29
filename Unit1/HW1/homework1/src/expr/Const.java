package expr;

import java.math.BigInteger;

public class Const implements Factor {
    private BigInteger num;

    public Const(BigInteger num) {
        this.num = num;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (num.compareTo(BigInteger.ZERO) > 0) {
            stringBuffer.append("+");
        }
        return stringBuffer.append(num).toString();
    }

    public BigInteger getNum() {
        return num;
    }

}
