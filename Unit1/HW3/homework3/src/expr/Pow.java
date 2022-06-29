package expr;

import java.util.Objects;

public class Pow extends Function {
    public Pow(String pow) {
        setIndex(pow);
    }

    @Override
    public String toString() {
        if (getIndex().toString().equals("0")) {
            return "1";
        } else if (getIndex().toString().equals("1")) {
            return "x";
        } else if (getIndex().toString().equals("2")) {
            return "x*x";
        }
        return "x**" + getIndex().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pow pow = (Pow) o;
        return Objects.equals(getIndex(), pow.getIndex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex());
    }

    @Override
    public Factor getBase() {
        return new Pow("1");
    }
}
