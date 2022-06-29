package expr;

import poly.Polynomial;
import poly.PolyTerm;

import java.math.BigInteger;

public class Number implements Factor {
    private BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public String toString() {
        return this.num.toString();
    }

    public BigInteger getNum() {
        return num;
    }

    public void reverse() {
        this.num = this.num.negate();
    }

    public Integer getInt() {
        return num.intValue();
    }

    @Override
    public Polynomial toPoly() {
        PolyTerm number = new PolyTerm(this.num);
        // number.addElement("x", 0);
        Polynomial polynomial = new Polynomial();
        polynomial.add(number);
        return polynomial;
    }

}
