package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Term {
    private ArrayList<Factor> factorArrayList = new ArrayList<>();
    private BigInteger coefficient = BigInteger.ONE;

    public void addFactor(Factor factor) {
        factorArrayList.add(factor);
    }

    public ArrayList<Factor> getFactorArrayList() {
        return factorArrayList;
    }

    public void setFactorArrayList(ArrayList<Factor> factorArrayList) {
        this.factorArrayList = factorArrayList;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public Term() {

    }

    public Term(BigInteger coefficient, HashSet<Factor> factors) {
        this.coefficient = coefficient;
        factorArrayList.addAll(factors);
    }

    /** (Expr) res -> (many Term) Term(const, pow, sin, cos)
     *  (Term) ret -> (Expr) res -> (many Term) Term(const, pow, sin, cos)
     */

    public Term mulTerm(Term other) {
        Term tmp = new Term();
        ArrayList<Factor> tmpArrayList = new ArrayList<>();
        tmpArrayList.addAll(other.getFactorArrayList());
        tmpArrayList.addAll(factorArrayList);
        tmp.setCoefficient(
                getCoefficient().multiply(other.getCoefficient()));
        for (Factor factor : tmpArrayList) {
            if (!(factor instanceof Expr)) {
                tmp.addFactor(factor);
            }
        }
        Expr res = new Expr();
        res.addTerm(tmp);
        for (Factor factor : tmpArrayList) {
            if (factor instanceof Expr) {
                res = res.mulExpr(((Expr) factor).expand());
            }
        }
        for (Term term : res.getTermArrayList()) {
            term.delConst();
            term.simplify();
        }
        res = res.simplify();
        Term ret = new Term();
        ret.addFactor(res);
        return ret;
    }

    /** (1 Term)ret -> (1 Expr)res -> (many Term)Term(const, pow, sin, cos) */
    public Term expand() {
        Term tmp = new Term();
        tmp.setCoefficient(this.getCoefficient());
        for (Factor factor : factorArrayList) {
            if (!(factor instanceof Expr) && !(factor instanceof Cos)
                    && !(factor instanceof Sin)) {
                tmp.addFactor(factor);
            } else if ((factor instanceof Cos)) {
                tmp.addFactor(((Cos) factor).expand());
            } else if ((factor instanceof Sin)) {
                Factor p = ((Sin) factor).expand(); 
                if (p instanceof Sin &&
                        ((Sin) p).getFactor() instanceof Const &&
                        ((Const) ((Sin) p).getFactor()).getNum().compareTo(BigInteger.ZERO) < 0) {
                    tmp.setCoefficient(tmp.getCoefficient().multiply(BigInteger.valueOf(-1)));
                    p = new Sin(new Const(((Const) ((Sin) p).getFactor()).getNum().abs()),
                            ((Sin) p).getIndex().toString());
                }
                tmp.addFactor(p);
            }
        }
        Expr res = new Expr();
        res.getTermArrayList().add(tmp);
        for (Factor factor : factorArrayList) {
            if (factor instanceof Expr) {
                res = res.mulExpr(((Expr) factor).expand());
            }
        }
        for (Term term : res.getTermArrayList()) {
            term.delConst();
            term.simplify();
        }
        res = res.simplify();
        Term ret = new Term();
        ret.addFactor(res);
        return ret;
    }

    public void delConst() {
        ArrayList<Factor> factors = new ArrayList<>();
        for (Factor factor : factorArrayList) {
            if (factor instanceof Const) {
                coefficient = coefficient.multiply(((Const)factor).getNum());
            } else {
                factors.add(factor);
            }
        }
        this.factorArrayList = factors;
    }

    public void simplify() {
        HashMap<Factor, BigInteger> hashMap = new HashMap<>();
        for (Factor factor : factorArrayList) {
            Factor base = factor.getBase();
            BigInteger index = ((Function)factor).getIndex();
            if (hashMap.containsKey(base)) {
                hashMap.put(base, hashMap.get(base).add(index));
            } else {
                hashMap.put(base, index);
            }
        }
        ArrayList<Factor> res = new ArrayList<>();
        hashMap.forEach((k, v) -> res.add(((Function)k).calculate(v)));
        res.removeIf(factor -> factor instanceof Function
                && ((Function)factor).getIndex().equals(BigInteger.ZERO)); /* remove*/
        this.factorArrayList = res;
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        if (coefficient.compareTo(BigInteger.ZERO) >= 0) {
            stringBuffer.append("+");
        } else {
            stringBuffer.append("-");
        }
        if (factorArrayList.isEmpty()) {
            stringBuffer.append(coefficient.abs());
            return stringBuffer.toString();
        } else if (!coefficient.abs().equals(BigInteger.ONE)) {
            stringBuffer.append(coefficient.abs()).append("*");
        }
        for (Factor factor : factorArrayList) {
            stringBuffer.append(factor.toString()).append("*");
        }
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Term term = (Term) o;
        return Objects.equals(factorArrayList, term.factorArrayList)
                && Objects.equals(coefficient, term.coefficient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factorArrayList, coefficient);
    }
}