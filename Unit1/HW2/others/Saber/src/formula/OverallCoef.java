package formula;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class OverallCoef implements Cloneable {
    private BigInteger coef;
    private ArrayList<Sin> sin = new ArrayList<>();
    private ArrayList<Cos> cos = new ArrayList<>();

    public BigInteger getCoef() {
        return coef;
    }

    public void setCoef(BigInteger coef) {
        this.coef = coef;
    }

    public ArrayList<Sin> getSin() {
        return sin;
    }

    public void setSin(ArrayList<Sin> sin) {
        this.sin = sin;
    }

    public ArrayList<Cos> getCos() {
        return cos;
    }

    public void setCos(ArrayList<Cos> cos) {
        this.cos = cos;
    }

    public OverallCoef(BigInteger coef, ArrayList<Sin> sin, ArrayList<Cos> cos) {
        this.coef = coef;
        this.sin = sin;
        this.cos = cos;
    }

    public OverallCoef(BigInteger coef, Sin sin, Cos cos) {
        this.coef = coef;
        this.sin.add(sin);
        this.cos.add(cos);
    }

    public void mulTri(Triangle tri) {
        Triangle trinew;
        boolean flag = false;
        if (tri instanceof Sin) {
            trinew = (Sin) tri;
            for (Sin s : sin) {
                if (s.getInIndex().equals(trinew.getInIndex())
                        && s.getConstant().equals(trinew.getConstant())) {
                    s.setOutIndex(s.getOutIndex().add(trinew.getOutIndex()));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                sin.add((Sin) trinew);
            }
        } else {
            trinew = (Cos) tri;
            for (Cos c : cos) {
                if (c.getInIndex().equals(trinew.getInIndex())
                        && c.getConstant().equals(trinew.getConstant())) {
                    c.setOutIndex(c.getOutIndex().add(trinew.getOutIndex()));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                cos.add((Cos) trinew);
            }
        }
    }

    public OverallCoef negate() {
        OverallCoef oc;
        oc = this.clone();
        oc.setCoef(oc.getCoef().negate());
        return oc;
    }

    public boolean enMerge(OverallCoef other) {
        Collections.sort(sin);
        Collections.sort(cos);
        Collections.sort(other.getSin());
        Collections.sort(other.getCos());
        boolean flag = true;
        if (sin.size() != other.getSin().size() || cos.size() != other.getCos().size()) {
            flag = false;
        }
        for (Sin s1 : other.getSin()) {
            for (Sin s2 : sin) {
                if (s1.compareTo(s2) != 0) {
                    flag = false;
                    break;
                }
            }
        }
        for (Cos c1 : other.getCos()) {
            for (Cos c2 : cos) {
                if (c1.compareTo(c2) != 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public OverallCoef clone() {
        try {
            OverallCoef clone = (OverallCoef) super.clone();
            //TODO: copy mutable state here, so the clone can't change the internals of the original
            ArrayList<Sin> als = new ArrayList<>();
            ArrayList<Cos> alc = new ArrayList<>();
            for (Sin s : sin) {
                als.add((Sin) s.clone());
            }
            for (Cos c : cos) {
                alc.add((Cos) c.clone());
            }
            clone = new OverallCoef(coef, als, alc);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
