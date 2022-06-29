import java.math.BigInteger;

public class Variable implements Factor {
    private BigInteger coefficient;
    private int power;

    public Variable(BigInteger coefficient, int power) {
        this.coefficient = coefficient;
        this.power = power;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public int getPower() {
        return power;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String toString() {
        if (power == 0) {
            return this.coefficient.toString();
        }
        return this.coefficient.toString() + "*x" + "**" + this.power;
    }
}
