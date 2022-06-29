package expr;

import poly.Polynomial;
import poly.PolyTerm;

import java.math.BigInteger;

public class PowerFunc implements Factor {
    private final String name;

    private final int power;

    public PowerFunc(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public String toString() {
        if (this.power == 1) {
            return this.name;
        } else {
            return this.name +
                    "**" +
                    this.power;
        }
    }

    @Override
    public Polynomial toPoly() {
        PolyTerm pf = new PolyTerm(BigInteger.ONE);
        pf.addElement(this.name, this.power);
        Polynomial polynomial = new Polynomial();
        polynomial.add(pf);
        return polynomial;
    }

}
