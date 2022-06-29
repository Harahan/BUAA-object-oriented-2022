import java.math.BigInteger;

public class Number implements Factor {
    private final BigInteger num;
    
    public Number(BigInteger num) {
        this.num = num;
    }
    
    public String toString() {
        return this.num.toString();
    }
    
    public Polynomial toPolynomial() {
        BodyMap bodyMap = new BodyMap("1",BigInteger.ZERO);
        Polynomial number = new Polynomial();
        number.extend(bodyMap,this.num);
        return number;
    }
}