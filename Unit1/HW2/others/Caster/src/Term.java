import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Factor {
    private final ArrayList<Factor> factors;
    
    public Term() {
        this.factors = new ArrayList<>();
    }
    
    public Term(BigInteger index,Factor expr) {
        this.factors = new ArrayList<>();
        if (index.equals(BigInteger.ZERO)) {
            Factor one = new Number(BigInteger.ONE);
            this.addFactor(one);
        }
        for (int i = 0; BigInteger.valueOf(i).compareTo(index) < 0; i++) {
            this.addFactor(expr);
        }
    }
    
    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }
    
    public Polynomial toPolynomial() {
        BodyMap bodyMap = new BodyMap("1",BigInteger.ZERO);
        Polynomial term = new Polynomial();
        term.extend(bodyMap,BigInteger.ONE);
        for (Factor i : factors) {
            term = term.mult(i.toPolynomial());
        }
        return term;
    }
}
