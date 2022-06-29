package expr;

import poly.PolyTerm;
import poly.Polynomial;

import java.math.BigInteger;

public class TriFunc implements Factor {
    private final String type;
    private final Factor core;
    private final int power;

    public TriFunc(String type, Factor core, int power) {
        this.type = type;
        this.core = core;
        this.power = power;
    }

    public String toString() {
        if (this.power == 1) {
            return this.type + "(" + core.toString() + ")";
        } else {
            return this.type + "(" + core.toString() + ")" + "**" + this.power;
        }

    }

    @Override
    public Polynomial toPoly() {
        final PolyTerm tf = new PolyTerm(BigInteger.ONE);

        String newCore = this.core.toPoly().toString();

        if (this.core instanceof Number) {
            if (((Number) this.core).getNum().compareTo(BigInteger.ZERO) < 0) {
                if (this.type.equals("cos")) {
                    ((Number) this.core).reverse();
                    newCore = this.core.toPoly().toString();
                }
            }
        }

        if (this.core instanceof Expr) {
            Polynomial innerPoly = this.core.toPoly();
            if (innerPoly.isSingle()) {
                newCore = innerPoly.toString();
            } else {
                newCore = "(" + innerPoly + ")";
            }
        }

        String base;
        base = this.type + "(" + newCore + ")";

        tf.addElement(base, this.power);
        Polynomial polynomial = new Polynomial();
        polynomial.add(tf);
        return polynomial;
    }
}
