package poly;

import java.math.BigInteger;
import java.util.HashMap;

public class PolyTerm {
    private BigInteger coefficient;
    private final HashMap<String, Integer> elements;

    public PolyTerm(BigInteger coefficient) {
        this.coefficient = coefficient;
        this.elements = new HashMap<>();
    }

    public int getSize() {
        return this.elements.size();
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public void addElement(String base, Integer power) {
        this.elements.put(base, power);
    }

    public HashMap<String, Integer> getElements() {
        return elements;
    }

    public PolyTerm multiply(PolyTerm other) {
        BigInteger newCo = this.getCoefficient().multiply(other.getCoefficient());
        PolyTerm polyTerm = new PolyTerm(newCo);
        polyTerm.elements.putAll(this.elements);
        for (String base : other.elements.keySet()) {
            if (polyTerm.getElements().containsKey(base)) {
                int newPower = polyTerm.getElements().get(base) + other.getElements().get(base);
                polyTerm.getElements().replace(base, newPower);
            } else {
                int power = other.getElements().get(base);
                polyTerm.getElements().put(base, power);
            }
        }
        return polyTerm;
    }

}
