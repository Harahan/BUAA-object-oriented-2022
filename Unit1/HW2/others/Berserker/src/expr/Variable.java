package expr;

import java.math.BigInteger;

public class Variable implements Factor {
    private BigInteger coefficient;
    private final long index;

    public Variable(BigInteger coefficient, long index) {
        this.coefficient = coefficient;
        this.index = index;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public long getIndex() {
        return index;
    }

    public boolean equal(Variable variable) {
        return (coefficient.equals(variable.coefficient) && index == variable.index);
    }
}
