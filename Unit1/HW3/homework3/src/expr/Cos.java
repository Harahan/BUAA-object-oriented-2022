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
                    factor = new Const(num.abs());
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
                && ((Const) factor).getNum().equals(BigInteger.ZERO)) {
            return new Const(BigInteger.ONE);
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
