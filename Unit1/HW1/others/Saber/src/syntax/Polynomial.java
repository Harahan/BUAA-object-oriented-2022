package syntax;

import java.math.BigInteger;
import java.util.Objects;

public class Polynomial
{
    private BigInteger coefficient;
    private BigInteger index;

    public Polynomial(BigInteger coefficient, BigInteger index)
    {
        this.coefficient = coefficient;
        this.index = index;
    }

    public BigInteger getCoefficient()
    {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient)
    {
        this.coefficient = coefficient;
    }

    public BigInteger getIndex()
    {
        return index;
    }

    public void setIndex(BigInteger index)
    {
        this.index = index;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Polynomial that = (Polynomial) o;
        return Objects.equals(coefficient, that.coefficient) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(coefficient, index);
    }
}
