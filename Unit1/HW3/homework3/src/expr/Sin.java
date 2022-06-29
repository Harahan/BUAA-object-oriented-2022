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

    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    public Factor getFactor() {
        return factor;
    }

    /**const | power | expr | cos | sin
     * power -> x ** a (a > 0)
     * const -> sin(a) ** 2k (a > 0) | sin(a) ** 2k + 1
     * expr -> expr - term(pow, sin..., cos...) (no const)
     * coefficient > 0
     */
    public Factor expand() {
        Term term;
        BigInteger num;
        Expr expr;
        if (factor instanceof Expr) {
            expr = ((Expr) factor).expand();
            if (expr.getTermArrayList().isEmpty()) {
                factor =  new Const(BigInteger.ZERO);
            } else if (expr.getTermArrayList().size() == 1) {
                term = expr.getTermArrayList().get(0);
                num = term.getCoefficient();
                if (term.getFactorArrayList().isEmpty()) {
                    if (getIndex().mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
                        factor = new Const(num.abs());
                    } else {
                        factor = new Const(num);
                    }
                } else if (num.equals(BigInteger.ONE)
                        && term.getFactorArrayList().size() == 1) {
                    factor = term.getFactorArrayList().get(0);
                } else {
                    factor = expr;
                }
            } else {
                factor = expr.mergeSquare();
            }
        } else if (factor instanceof Sin) {
            factor = ((Sin) factor).expand();
        } else if (factor instanceof Cos) {
            factor = ((Cos) factor).expand();
        }
        if (factor instanceof Const
                && ((Const) factor).getNum().equals(BigInteger.ZERO)
                && !this.getIndex().equals(BigInteger.ZERO)) {
            return new Const(BigInteger.ZERO);
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
