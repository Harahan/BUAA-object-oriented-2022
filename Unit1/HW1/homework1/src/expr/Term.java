package expr;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private ArrayList<Factor> factorArrayList = new ArrayList<>();

    public void addFactor(Factor factor) {
        factorArrayList.add(factor);
    }

    public ArrayList<Factor> getFactorArrayList() {
        return factorArrayList;
    }

    public void setFactorArrayList(ArrayList<Factor> factorArrayList) {
        this.factorArrayList = factorArrayList;
    }

    public void toExprBrackets() { // (Exp), (Exp), const, pow ......
        ArrayList<Factor> arrayList = new ArrayList<>();
        for (Factor item : factorArrayList) {
            if (item instanceof Expr) {
                BigInteger bigInteger = ((Expr) item).getIndex();
                if (bigInteger.compareTo(BigInteger.ZERO) > 0) {
                    for (int i = 1; i <= bigInteger.intValue(); i++) {
                        arrayList.
                                add(new Expr(((Expr) item).getTermArrayList(),
                                        ((Expr) item).getIndex()));
                    }
                }
            } else {
                arrayList.add(item);
            }
        }
        this.setFactorArrayList(arrayList);
    }

    public void mulFactorPowConst() { // (expr)'s index greater than 0
        ArrayList<Factor> arrayList = new ArrayList<>();
        BigInteger coeff = BigInteger.ONE;
        BigInteger index = BigInteger.ZERO;
        for (Factor item : factorArrayList) {
            if (item instanceof Pow) {
                index = index.add(((Pow) item).getIndex());
            } else if (item instanceof Const) {
                coeff = coeff.multiply(((Const) item).getNum());
            } else {
                arrayList.add(item);
            }
        }
        arrayList.add(new Pow(index.toString()));
        arrayList.add(new Const(coeff));
        this.setFactorArrayList(arrayList); // pow, (exp), const...
    }

    public Term mulTerm(Term other) {
        Term tmp = new Term();
        ArrayList<Factor> tmpArrayList = new ArrayList<>();

        tmpArrayList.addAll(other.getFactorArrayList());
        tmpArrayList.addAll(factorArrayList);

        for (int i = 0; i < tmpArrayList.size(); i++) {
            if (!(tmpArrayList.get(i) instanceof Expr)) {
                tmp.addFactor(tmpArrayList.get(i));
            }
        }
        tmp.mulFactorPowConst(); // (Term) tmp ->  const | pow, const
        Expr res = new Expr();
        res.addTerm(tmp);
        for (int i = 0; i < tmpArrayList.size(); i++) {
            if (tmpArrayList.get(i) instanceof Expr) {
                res = res.mulExpr(((Expr) tmpArrayList.get(i)).expand());
            }
        }
        // (Expr) res -> (many Term) Term(const | const, pow)
        res.simplify();
        Term ret = new Term();
        ret.addFactor(res);
        return ret; // (Term) ret -> (Expr) res -> (many Term) Term(const | const, pow)
    }

    public Term expand() {
        Term tmp = new Term();
        for (int i = 0; i < factorArrayList.size(); i++) {
            if (!(factorArrayList.get(i) instanceof Expr)) {
                tmp.addFactor(factorArrayList.get(i));
            }
        }
        tmp.mulFactorPowConst();
        Expr res = new Expr();
        res.getTermArrayList().add(tmp);
        for (int i = 0; i < factorArrayList.size(); i++) {
            if (factorArrayList.get(i) instanceof Expr) {
                res = res.mulExpr(((Expr) factorArrayList.get(i)).expand());
            }
        }
        res.simplify();
        Term ret = new Term();
        ret.addFactor(res);
        return ret; // (1 Term)ret -> (1 Expr)res -> (many Term)Term(const | const, pow)
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (factorArrayList.size() == 1 && factorArrayList.get(0) instanceof Pow) {
            stringBuffer.append("+" + factorArrayList.get(0).toString());
        } else if (factorArrayList.size() == 1) {
            stringBuffer.append(factorArrayList.get(0).toString());
        } else {
            Const num = null;
            Pow pow = null;
            for (Factor factor : factorArrayList) {
                if (factor instanceof Const) {
                    num = (Const) factor;
                } else {
                    pow = (Pow) factor;
                }
            }
            if (num.getNum().equals(BigInteger.valueOf(-1))) {
                stringBuffer.append("-" + pow.toString());
            } else {
                stringBuffer.append(num + "*" + pow.toString());
            }
        }
        return stringBuffer.toString();
    }
}