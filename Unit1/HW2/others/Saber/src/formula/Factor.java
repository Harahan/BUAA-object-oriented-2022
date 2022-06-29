package formula;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Factor implements Cloneable {
    private HashMap<BigInteger, ArrayList<OverallCoef>> table = new HashMap<>();

    public Factor() {
    }

    public Factor(Term term, boolean isSin) {
        BigInteger inIndex = BigInteger.ZERO;
        BigInteger constant = BigInteger.ZERO;
        for (BigInteger i : term.getTable().keySet()) {
            inIndex = i;
            constant = term.getTable().get(i).get(0).getCoef();
        }
        if (isSin) {
            append(BigInteger.ZERO, new OverallCoef(BigInteger.ONE,
                    new Sin(BigInteger.ONE, inIndex, constant),
                    new Cos(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO)));
        } else {
            append(BigInteger.ZERO, new OverallCoef(BigInteger.ONE,
                    new Sin(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO),
                    new Cos(BigInteger.ONE, inIndex, constant)));
        }

    }

    public void append(BigInteger index, OverallCoef overallCoef) {
        if (table.containsKey(index)) {
            boolean flag = false;
            for (OverallCoef oc : table.get(index)) {
                if (oc.enMerge(overallCoef)) {
                    oc.setCoef(oc.getCoef().add(overallCoef.getCoef()));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                table.get(index).add(overallCoef);
            }
        } else {
            ArrayList<OverallCoef> al = new ArrayList<>();
            al.add(overallCoef);
            table.put(index, al);
        }
    }

    public void append(BigInteger index, BigInteger coef, BigInteger outIndex,
                       BigInteger inIndex, BigInteger constant, boolean flag) {
        if (flag) {
            Sin tri = new Sin(outIndex, inIndex, constant);
            OverallCoef oc = new OverallCoef(coef, tri,
                    new Cos(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO));
            append(index, oc);
        } else {
            Cos tri = new Cos(outIndex, inIndex, constant);
            OverallCoef oc = new OverallCoef(coef,
                    new Sin(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO), tri);
            append(index, oc);
        }
    }

    public void append(BigInteger index, ArrayList<OverallCoef> al) {
        for (OverallCoef oc : al) {
            append(index, oc);
        }
    }

    public HashMap<BigInteger, ArrayList<OverallCoef>> getTable() {
        return table;
    }

    public Factor power(BigInteger index) {
        Factor factor = new Factor();
        ArrayList<OverallCoef> al = new ArrayList<>();
        factor.append(BigInteger.ZERO,
                BigInteger.ONE, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, true);
        for (BigInteger i = BigInteger.ZERO; i.compareTo(index) < 0; i = i.add(BigInteger.ONE)) {
            factor = factor.mul(this);
        }
        return factor;
    }

    public Factor mul(Factor other) {
        HashMap<BigInteger, ArrayList<OverallCoef>> tableOfOther = other.getTable();
        Factor factor = new Factor();
        for (BigInteger index1 : table.keySet()) {
            for (BigInteger index2 : tableOfOther.keySet()) {
                BigInteger index = index1.add(index2);
                ArrayList<OverallCoef> al1 = table.get(index1);
                ArrayList<OverallCoef> al2 = tableOfOther.get(index2);
                ArrayList<OverallCoef> alnew = new ArrayList<>();
                BigInteger coefficient;
                for (OverallCoef oc1 : al1) {
                    for (OverallCoef oc2 : al2) {
                        OverallCoef oc1new = oc1.clone();
                        OverallCoef oc2new = oc2.clone();
                        coefficient = oc1new.getCoef().multiply(oc2new.getCoef());
                        ArrayList<Sin> sin = oc2new.getSin();
                        ArrayList<Cos> cos = oc2new.getCos();
                        for (Sin s : sin) {
                            oc1new.mulTri(s);
                        }
                        for (Cos c : cos) {
                            oc1new.mulTri(c);
                        }
                        OverallCoef ocnew = new OverallCoef(coefficient,
                                oc1new.getSin(), oc1new.getCos());
                        factor.append(index, ocnew);
                    }
                }
            }
        }
        return factor;
    }

    public String toString() {
        Iterator it = table.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        if (it.hasNext()) {
            do {
                boolean tag = false;
                Map.Entry entry = (Map.Entry) it.next();
                BigInteger index = (BigInteger) entry.getKey();
                ArrayList<OverallCoef> al = table.get(index);
                cnt = 0;
                for (OverallCoef oc : al) {
                    if (!oc.getCoef().equals(BigInteger.ZERO)) {
                        tag = true;
                    }
                }
                if (tag) {
                    sb.append("+");
                }
                for (OverallCoef oc : al) {
                    boolean hasTri = false;
                    BigInteger coefficient = oc.getCoef();
                    ArrayList<Sin> sin = oc.getSin();
                    ArrayList<Cos> cos = oc.getCos();
                    if (cnt != 0 && oc.getCoef().compareTo(BigInteger.ZERO) > 0) {
                        sb.append("+");
                    }
                    ///外层:有输出的情况
                    if (!coefficient.equals(BigInteger.ZERO)) {
                        /////内层1：要输出数字的情况
                        if (!coefficient.equals(BigInteger.ONE)
                                && !coefficient.equals(BigInteger.ONE.negate())) {
                            sb.append(coefficient);
                        } else if (index.equals(BigInteger.ZERO)) {
                            sb.append(coefficient);
                        } else if (coefficient.equals(BigInteger.ONE.negate())) {
                            sb.append("-");
                        }
                        ////内层2：要输出x的情况
                        if (!index.equals(BigInteger.ZERO)) {
                            if (!coefficient.equals(BigInteger.ONE)
                                    && !coefficient.equals(BigInteger.ONE.negate())) {
                                sb.append("*");
                            }
                            sb.append("x");
                            if (!index.equals(BigInteger.ONE)) {
                                if (!index.equals(BigInteger.ONE.add(BigInteger.ONE))) {
                                    sb.append("**" + index);
                                } else {
                                    sb.append("*x");
                                }
                            }
                        }
                        for (Sin k : sin) {
                            if (!k.getOutIndex().equals(BigInteger.ZERO)) {
                                hasTri = true;
                            }
                        }
                        for (Cos k : cos) {
                            if (!k.getOutIndex().equals(BigInteger.ZERO)) {
                                hasTri = true;
                            }
                        }
                        if (hasTri) {
                            if (!sin.isEmpty()) {
                                sb.append("*");
                                Iterator itsin = sin.iterator();
                                Sin s = (Sin) itsin.next();
                                BigInteger outIndex = s.getOutIndex();
                                BigInteger inIndex = s.getInIndex();
                                BigInteger constant = s.getConstant();
                                boolean flag = false;
                                do {
                                    flag = false;
                                    //外层： 要输出sin的情况
                                    if (!outIndex.equals(BigInteger.ZERO)) {
                                        sb.append("sin(");
                                        if (!inIndex.equals(BigInteger.ZERO)) {
                                            sb.append("x");
                                            if (!inIndex.equals(BigInteger.ONE)) {
                                                sb.append("**" + inIndex);
                                            }
                                        } else {
                                            sb.append(constant);
                                        }
                                        sb.append(")");
                                        if (!outIndex.equals(BigInteger.ONE)) {
                                            sb.append("**" + outIndex);
                                        }
                                    } else {
                                        sb.append("1");
                                        if (itsin.hasNext()) {
                                            flag = true;
                                            s = (Sin) itsin.next();
                                            outIndex = s.getOutIndex();
                                            inIndex = s.getInIndex();
                                            constant = s.getConstant();
                                            sb.append("*");
                                        } else {
                                            break;
                                        }
                                    }
                                } while (flag);
                            }
                            if (!cos.isEmpty()) {
                                sb.append("*");
                                Iterator itcos = cos.iterator();
                                Cos c = (Cos) itcos.next();
                                BigInteger outIndex = c.getOutIndex();
                                BigInteger inIndex = c.getInIndex();
                                BigInteger constant = c.getConstant();
                                boolean flag = false;
                                do {
                                    flag = false;
                                    //外层： 要输出cos的情况
                                    if (!outIndex.equals(BigInteger.ZERO)) {
                                        sb.append("cos(");
                                        if (!inIndex.equals(BigInteger.ZERO)) {
                                            sb.append("x");
                                            if (!inIndex.equals(BigInteger.ONE)) {
                                                sb.append("**" + inIndex);
                                            }
                                        } else {
                                            sb.append(constant);
                                        }
                                        sb.append(")");
                                        if (!outIndex.equals(BigInteger.ONE)) {
                                            sb.append("**" + outIndex);
                                        }
                                    } else {
                                        sb.append("1");
                                    }
                                    if (itcos.hasNext()) {
                                        flag = true;
                                        c = (Cos) itcos.next();
                                        outIndex = c.getOutIndex();
                                        inIndex = c.getInIndex();
                                        constant = c.getConstant();
                                        sb.append("*");
                                    }
                                } while (flag);
                            }
                        }
                        cnt++;
                    }
                }
            } while (it.hasNext());
        }
        String s = sb.toString();
        if (!s.equals("")) {
            if ("+".indexOf(s.charAt(0)) != -1) {
                s = s.substring(1);
            }
            s = s.replace("+-", "-");
            s = s.replace("-+", "-");
            s = s.replace("++", "+");
            s = s.replace("--", "+");
            s = s.replace("+-", "-");
            s = s.replace("-+", "-");
            s = s.replace("++", "+");
            s = s.replace("--", "+");
        }
        return s.equals("") ? "0" : s;
    }

    @Override
    public Factor clone() {
        try {
            Factor clone = (Factor) super.clone();
            //TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
