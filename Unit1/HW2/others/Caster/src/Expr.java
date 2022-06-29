import java.math.BigInteger;
import java.util.HashSet;

public class Expr implements Factor {
    
    private final HashSet<Term> terms;
    
    public Expr() {
        this.terms = new HashSet<>();
    }
    
    public void addTerm(Term term) {
        this.terms.add(term);
    }
    
    public Polynomial toPolynomial() {
        BodyMap bodyMap = new BodyMap("1",BigInteger.ZERO);
        Polynomial expr = new Polynomial();
        expr.extend(bodyMap,BigInteger.ZERO);
        
        for (Term i : terms) {
            expr.add(i.toPolynomial());
        }
        return expr;
    }
}
