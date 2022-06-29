package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import poly.PolyTerm;
import poly.Polynomial;

public class Term {
    private boolean negative;

    private final ArrayList<Factor> factors;

    public Term() {
        this.negative = false;
        this.factors = new ArrayList<>();
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public boolean isNegative() {
        return negative;
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        
        if (iter.hasNext()) {
            // sb.append("*");
            Factor curFact1 = iter.next();
            if (curFact1 instanceof Expr) {
                sb.append("(");
                sb.append(curFact1);
                sb.append(")");
            } else {
                sb.append(curFact1.toString());
            }

            while (iter.hasNext()) {
                sb.append("*");
                Factor curFact2 = iter.next();
                if (curFact2 instanceof Expr) {
                    sb.append("(");
                    sb.append(curFact2);
                    sb.append(")");
                } else {
                    sb.append(curFact2);
                }
            }
        }
        return sb.toString();
    }

    public Polynomial toPoly() {
        Iterator<Factor> iter = factors.iterator();
        Polynomial polynomial = new Polynomial();
        PolyTerm polyTerm = new PolyTerm(BigInteger.ONE);
        polynomial.add(polyTerm);

        if (iter.hasNext()) {
            Factor curFact1 = iter.next();
            Polynomial curPoly1 = curFact1.toPoly();
            polynomial.multiply(curPoly1);

            while (iter.hasNext()) {
                Factor curFact2 = iter.next();
                Polynomial curPoly2 = curFact2.toPoly();
                polynomial.multiply(curPoly2);
            }
        }

        return polynomial;
    }
}
