package formula;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Term extends Expression {
    public Term() {
    }

    public Term mul(Factor other) {
        HashMap<BigInteger, ArrayList<OverallCoef>> tableOfOther = other.getTable();
        Term term = new Term();
        for (BigInteger index1 : getTable().keySet()) {
            for (BigInteger index2 : tableOfOther.keySet()) {
                BigInteger index = index1.add(index2);
                ArrayList<OverallCoef> al1 = getTable().get(index1);
                ArrayList<OverallCoef> al2 = tableOfOther.get(index2);
                ArrayList<OverallCoef> alnew = new ArrayList<>();
                BigInteger coefficient;
                OverallCoef oc1new;
                OverallCoef oc2new;
                for (OverallCoef oc1 : al1) {
                    for (OverallCoef oc2 : al2) {
                        oc1new = oc1.clone();
                        oc2new = oc2.clone();
                        coefficient = oc1new.getCoef().multiply(oc2new.getCoef());
                        ArrayList<Sin> sin = oc2new.getSin();
                        ArrayList<Cos> cos = oc2new.getCos();
                        for (Sin s : sin) {
                            oc1new.mulTri(s);
                        }
                        for (Cos c : cos) {
                            oc1new.mulTri(c);
                        }
                        OverallCoef ocnew = new OverallCoef(coefficient, oc1new.getSin(),
                                oc1new.getCos());
                        alnew.add(ocnew);
                    }
                }
                term.append(index, alnew);
            }
        }
        return term;
    }

    public ArrayList negate(BigInteger index) {
        HashMap<BigInteger, ArrayList<OverallCoef>> table = getTable();
        ArrayList<OverallCoef> al = table.get(index);
        ArrayList<OverallCoef> alnew = new ArrayList<>();
        for (OverallCoef oc : al) {
            OverallCoef ocnew = oc.clone();
            ocnew.setCoef(ocnew.getCoef().negate());
            alnew.add(ocnew);
        }
        return alnew;
    }

    public Term(Factor factor) {
        HashMap<BigInteger, ArrayList<OverallCoef>> tableOfFactor = factor.getTable();
        Set<BigInteger> indexSet = tableOfFactor.keySet();
        for (BigInteger index : indexSet) {
            ArrayList<OverallCoef> al = new ArrayList<>();
            for (OverallCoef oc : tableOfFactor.get(index)) {
                al.add(oc.clone());
            }
            getTable().put(index, al);
        }
    }

}
