import java.math.BigInteger;

public class PowerFunc implements Factor {
    private BigInteger index;
    private boolean neg;
    
    public PowerFunc(boolean neg, BigInteger index) {
        this.neg = neg;
        this.index = index;
    }
    
    public Polynomial toPolynomial() {
        int factor = !neg ? 1 : -1;
        Polynomial powerFunc = new Polynomial();
        BodyMap bodyMap = new BodyMap("x",index);
        powerFunc.extend(bodyMap,BigInteger.valueOf(factor));
        return powerFunc;
    }
}
