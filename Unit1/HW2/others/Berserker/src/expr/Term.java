package expr;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private final ArrayList<Factor> factors;
    private long index;
    private BigInteger coefficient;
    private ArrayList<Trigo> trigos;
    private Boolean symbol;
    private boolean merge;

    public Term(long index, BigInteger coefficient) {
        this.index = index;
        this.coefficient = coefficient;
        factors = new ArrayList<>();
        trigos = new ArrayList<>();
        symbol = true;
        merge = true;
    }

    public Term(Term term) {
        index = term.index;
        coefficient = term.coefficient;
        factors = term.factors;
        trigos = term.trigos;
        symbol = term.symbol;
        merge = term.merge;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setSymbol(Boolean symbol) {
        this.symbol = symbol;
    }

    public Boolean getSymbol() {
        return symbol;
    }

    public long getIndex() {
        return index;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public void addFactor(Factor factor) {
        if (factor instanceof Trigo) {
            trigos.add((Trigo) factor);
        } else {
            factors.add(factor);
        }
    }

    public ArrayList<Trigo> getTrigos() {
        return trigos;
    }

    public void setTrigos(ArrayList<Trigo> trigos) {
        this.trigos = trigos;
    }

    public void merge(Term term) {
        if (similarTo(term)) {
            coefficient = coefficient.add(term.coefficient);
            merge = false;
            term.merge = false;
        }
    }

    public boolean similarTo(Term term) {
        if (index == term.index) {
            ArrayList<Trigo> trigots = new ArrayList<>();
            for (Trigo trigo:term.trigos) {
                trigots.add(new Trigo(trigo));
            }
            for (Trigo trigo:trigos) {
                int flag = 0;
                for (Trigo trigo1:trigots) {
                    if (trigo1.equals(trigo)) {
                        trigots.remove(trigo1);
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    return false;
                }
            }
            if (trigots.size() == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void mergeTrigo() {
        ArrayList<Trigo> newtrigos = new ArrayList<>();
        ArrayList<Trigo> trigois = new ArrayList<>();
        for (Trigo trigo:trigos) {
            trigois.add(new Trigo(trigo));
        }
        for (int i = 0;i < trigois.size();i++) {
            if (trigois.get(i).isMerge()) {
                for (int j = i + 1;j < trigois.size();j++) {
                    if (trigois.get(j).isMerge()) {
                        trigois.get(i).merge(trigois.get(j));
                    }
                }
                newtrigos.add(trigois.get(i));
            }
        }
        trigos = newtrigos;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean flag = trigos.size() > 0;
        if (this.coefficient.equals(new BigInteger("0"))) {
            return "0";
        }
        if (this.coefficient.equals(new BigInteger("-1"))) {
            if (this.index == 0) {
                if (flag) {
                    sb.append("-");
                } else {
                    return "-1";
                }
            } else if (this.index == 1) {
                sb.append("-x");
            } else if (this.index == 2) {
                sb.append("-x*x");
            } else {
                sb.append("-x**" + index);
            }
        } else if (this.coefficient.equals(new BigInteger("1"))) {
            if (this.index == 0) {
                if (!flag) {
                    return "1";
                }
            } else if (this.index == 1) {
                sb.append("x");
            } else if (this.index == 2) {
                sb.append("x*x");
            } else {
                sb.append("x**" + index);
            }
        } else {
            sb.append(coefficient);
            if (this.index == 1) {
                sb.append("*x");
            } else if (this.index == 2) {
                sb.append("*x*x");
            } else if (this.index != 0) {
                sb.append("*x**" + index);
            }
        }
        if (flag) {
            if (!sb.toString().equals("") && !sb.toString().equals("-")) {
                sb.append("*");
            }
            sb.append(trigos.get(0).toString());
            for (int i = 1; i < trigos.size(); i++) {
                sb.append("*" + trigos.get(i).toString());
            }
        }
        return sb.toString();
    }
}
