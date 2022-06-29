package expr;

import poly.Polynomial;

import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public ArrayList<Term> getTerms() {
        return this.terms;
    }

    public boolean isSingle() {
        boolean single = (this.terms.size() == 1) &&
                (this.terms.get(0).getFactors().size() == 1);
        return single;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();

        sb.append("(");

        if (iter.hasNext()) {
            Term curTerm1 = iter.next();

            if (curTerm1.isNegative()) {
                sb.append("-");
            } else {
                sb.append("+");
            }
            sb.append(curTerm1);
            while (iter.hasNext()) {
                Term curTerm2 = iter.next();
                if (curTerm2.isNegative()) {
                    sb.append("-");
                } else {
                    sb.append("+");
                }
                sb.append(curTerm2);
            }
        }

        sb.append(")");

        return sb.toString();
    }

    @Override
    public Polynomial toPoly() {
        Iterator<Term> iter = terms.iterator();
        Polynomial polynomial = new Polynomial();

        if (iter.hasNext()) {
            Term curTerm1 = iter.next();
            Polynomial curPoly1 = curTerm1.toPoly();
            if (curTerm1.isNegative()) {
                curPoly1.reverse();
            }
            polynomial.combine(curPoly1);

            while (iter.hasNext()) {
                Term curTerm2 = iter.next();
                Polynomial curPoly2 = curTerm2.toPoly();
                if (curTerm2.isNegative()) {
                    curPoly2.reverse();
                }
                polynomial.combine(curPoly2);
            }
        }

        return polynomial;
    }
}
