package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Cos extends Function {
    private Factor factor;

    public Cos(Factor factor, String bracketRight) {
        this.factor = factor;
        this.setIndex(bracketRight);
        if (factor instanceof Const) {
            this.factor = new Const(((Const) factor).getNum().abs());
        } else if (factor instanceof Pow &&
                ((Pow) factor).getIndex().equals(BigInteger.ZERO)) {
            this.factor = new Const(BigInteger.ONE);
        }
    }

    public Factor expand() {
        Term term;
        BigInteger tmp;
        if (factor instanceof Expr) {
            factor = ((Expr) factor).expand();
            if (((Expr) factor).getTermArrayList().isEmpty()) {
                factor =  new Const(BigInteger.ZERO);
            } else {
                term = ((Expr) factor).getTermArrayList().get(0);
                if (term.getFactorArrayList().isEmpty()) {
                    tmp = term.getCoefficient();
                    if (tmp.equals(BigInteger.ZERO)) {
                        factor = new Const(BigInteger.ZERO);
                    } else {
                        factor = new Const(tmp.abs());
                    }
                } else {
                    factor = term.getFactorArrayList().get(0);
                }
            }
        }
        return this;
    }

    public Factor getFactor() {
        return factor;
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        if (factor.toString().equals("x*x")) {
            stringBuffer.append("cos(x**2)");
        } else {
            stringBuffer.append("cos(").append(factor.toString()).append(")");
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
        Cos cos = (Cos) o;
        return Objects.equals(getIndex(), cos.getIndex()) && Objects.equals(factor, cos.factor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), factor);
    }

    @Override
    public Factor getBase() {
        return new Cos(getFactor(), "1");
    }
}
