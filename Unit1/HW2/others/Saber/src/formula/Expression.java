package formula;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Expression extends Factor {
    public Expression() {
        ;
    }

    public void add(Expression other) {
        HashMap<BigInteger, ArrayList<OverallCoef>> table = super.getTable();
        HashMap<BigInteger, ArrayList<OverallCoef>> tableOfOther = other.getTable();
        for (BigInteger index : tableOfOther.keySet()) {
            for (OverallCoef oc : tableOfOther.get(index)) {
                append(index, oc);
            }
        }
    }

    public void sub(Term other) {
        HashMap<BigInteger, ArrayList<OverallCoef>> table = super.getTable();
        HashMap<BigInteger, ArrayList<OverallCoef>> tableOfOther = other.getTable();
        for (BigInteger index : tableOfOther.keySet()) {
            for (OverallCoef oc : tableOfOther.get(index)) {
                oc.setCoef(oc.getCoef().negate());
                append(index, oc);
            }
        }
    }

}


