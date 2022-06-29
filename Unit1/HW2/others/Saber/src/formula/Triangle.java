package formula;

import java.math.BigInteger;

public class Triangle implements Cloneable, Comparable<Triangle> {
    private BigInteger outIndex;
    private BigInteger inIndex;
    private BigInteger constant;

    public Triangle(BigInteger outIndex, BigInteger inIndex, BigInteger constant) {
        this.outIndex = outIndex;
        this.inIndex = inIndex;
        this.constant = constant;
    }

    public BigInteger getOutIndex() {
        return outIndex;
    }

    public void setOutIndex(BigInteger outIndex) {
        this.outIndex = outIndex;
    }

    public BigInteger getInIndex() {
        return inIndex;
    }

    public void setInIndex(BigInteger intIndex) {
        this.inIndex = intIndex;
    }

    public BigInteger getConstant() {
        return constant;
    }

    public void setConstant(BigInteger constant) {
        this.constant = constant;
    }

    @Override
    public Triangle clone() {
        try {
            Triangle clone = (Triangle) super.clone();
            //TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int compareTo(Triangle o) {
        BigInteger outIndexO = o.getOutIndex();
        BigInteger inIndexO = o.getInIndex();
        BigInteger constantO = o.getConstant();
        return outIndex.equals(outIndexO) ? (inIndex.equals(inIndexO) ?
                constant.compareTo(o.constant) :
                inIndex.compareTo(inIndexO)) :
                outIndex.compareTo(outIndexO);
    }

    @Override
    public String toString() {
        return "(" + constant + "*x**" + inIndex + ")**" + outIndex;
    }
}
