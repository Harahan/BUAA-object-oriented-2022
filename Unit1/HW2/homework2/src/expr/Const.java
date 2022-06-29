package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Const implements Factor {
    private final BigInteger num;

    @Override
    public Factor getBase() {
        return new Const(BigInteger.ONE);
    }

    public Const(BigInteger num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return String.valueOf(num);
    }

    public BigInteger getNum() {
        return num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Const aaConst = (Const) o;
        return Objects.equals(num, aaConst.num);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num);
    }
}
