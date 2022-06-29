package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Sin extends Function {
    private Factor factor;

    public Sin(Factor factor, String bracketRight) {
        this.factor = factor;
        this.setIndex(bracketRight);
        if (factor instanceof Const &&
                getIndex().mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            this.factor = new Const(((Const) factor).getNum().abs());
        } else if (factor instanceof Pow &&
                ((Pow) factor).getIndex().equals(BigInteger.ZERO)) {
            this.factor = new Const(BigInteger.ONE);
        }
    }

    public Factor getFactor() {
        return factor;
    }

    public Factor expand() {
        Term term;
        BigInteger tmp;
        if (factor instanceof Expr) {
            factor = ((Expr) factor).expand();
            if (((Expr) factor).getTermArrayList().isEmpty()) {
                factor = new  Const(BigInteger.ZERO);
            } else {
                term = ((Expr) factor).getTermArrayList().get(0);
                if (term.getFactorArrayList().isEmpty()) {
                    tmp = term.getCoefficient();
                    if (tmp.equals(BigInteger.ZERO)) {
                        factor = new Const(BigInteger.ZERO);
                    } else {
                        factor = new Const(tmp);
                        if (getIndex().mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
                            factor = new Const(((Const) factor).getNum().abs());
                        }
                    }
                } else {
                    factor = term.getFactorArrayList().get(0);
                }
            }
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        if (factor.toString().equals("x*x")) {
            stringBuffer.append("sin(x**2)");
        } else {
            stringBuffer.append("sin(").append(factor.toString()).append(")");
        }
        if (getIndex().equals(BigInteger.ONE)) {
            return stringBuffer.toString();
        } else if (getIndex().equals(BigInteger.ZERO)) {
            return "1";
        }
        return stringBuffer.append("**").append(getIndex()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sin sin = (Sin) o;
        return Objects.equals(getIndex(), sin.getIndex()) && Objects.equals(factor, sin.factor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), factor);
    }

    @Override
    public Factor getBase() {
        return new Sin(getFactor(), "1");
    }
}
