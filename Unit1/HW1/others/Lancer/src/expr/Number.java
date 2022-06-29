package expr;

import java.math.BigInteger;
import java.util.HashMap;

public class Number implements Factor {
    private BigInteger coef;
    private BigInteger exp;

    public Number(BigInteger coef, BigInteger exp) {
        this.coef = coef;
        this.exp = exp;
    }

    public String toString() {
        return this.coef.toString();
    }

    public HashMap<BigInteger, BigInteger> cal() {
        HashMap<BigInteger, BigInteger> num = new HashMap<>();
        num.put(exp,coef);
        return num;
    }

    public BigInteger abs() {
        return coef.abs();
    }

    public BigInteger getCoef() {
        return coef;
    }

    public BigInteger getExp() {
        return exp;
    }
}
