package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expr implements Factor {
    private ArrayList<Term> termArrayList = new ArrayList<>();
    private BigInteger index = new BigInteger("1");
    private static final Pattern PATTERNINDEX = Pattern.compile("[+]?\\d+");

    public Expr() {

    }

    public Expr(ArrayList<Term> termArrayList, BigInteger index) {
        this.termArrayList = termArrayList;
        this.index = index;
    }

    public Expr(Expr expr, String bracketsRight) {
        termArrayList = expr.getTermArrayList();
        setIndex(bracketsRight);
    }

    public void setIndex(String bracketsRight) {
        Matcher matcher = PATTERNINDEX.matcher(bracketsRight);
        matcher.find();
        index = new BigInteger(matcher.group());
    }

    public BigInteger getIndex() {
        return index;
    }

    public ArrayList<Term> getTermArrayList() {
        return termArrayList;
    }

    public void addTerm(Term term) {
        termArrayList.add(term);
    }

    public Expr mulExpr(Expr other) {
        Expr res = new Expr();
        for (Term myTerm : termArrayList) {
            for (Term otherTerm : other.termArrayList) {
                res.termArrayList.addAll(((Expr) myTerm.mulTerm(otherTerm).
                        getFactorArrayList().get(0)).termArrayList);
            }
        }
        res.simplify();
        return res; // (Expr)res -> (many Term) Term(const | const, pow)
    }

    public Expr expand() {
        Expr res = new Expr();
        for (int i = 0; i < termArrayList.size(); i++) {
            res.getTermArrayList().
                    addAll(((Expr) termArrayList.get(i).expand().
                            getFactorArrayList().get(0)).termArrayList);
            // (termArrayList.get(i).expand())
            // (1)Term -> (1)Expr -> (many)Term(const | pow | const, pow)
        }
        res.simplify();
        return res; // res: Expr -> term(const| const, pow)
    }

    public BigInteger getExp(Term term) {
        BigInteger exp = BigInteger.ZERO;
        for (Factor factor : term.getFactorArrayList()) {
            if (factor instanceof Pow) {
                exp = ((Pow) factor).getIndex();
            }
        }
        return exp;
    }

    public BigInteger getCoeff(Term term) {
        BigInteger coeff = BigInteger.ONE;
        for (Factor factor : term.getFactorArrayList()) {
            if (factor instanceof Const) {
                coeff = ((Const) factor).getNum();
            }
        }
        return coeff;
    }

    public void simplify() { // const, [pow]
        HashMap<BigInteger, BigInteger> hashMap = new HashMap<>();
        ArrayList<Term> tmp = new ArrayList<>();
        for (Term term : termArrayList) {
            BigInteger coeff = getCoeff(term);
            BigInteger exp = getExp(term);
            if (hashMap.containsKey(exp)) {
                hashMap.put(exp, hashMap.get(exp).add(coeff));
            } else {
                hashMap.put(exp, coeff);
            }
        }

        for (BigInteger exp : hashMap.keySet()) {
            BigInteger coeff = hashMap.get(exp);
            Term term = new Term();
            if (coeff.equals(BigInteger.ZERO)) {
                continue;
            } else if (exp.equals(BigInteger.ZERO)) {
                term.addFactor(new Const(coeff));
            } else if (coeff.equals(BigInteger.ONE)) {
                term.addFactor(new Pow(exp.toString()));
            } else { // exp > 0, coeff != 0 and coeff != 1
                term.addFactor(new Const(coeff));
                term.addFactor(new Pow(exp.toString()));
            }
            tmp.add(term);
        }
        this.termArrayList = tmp;
    }

    public int getFirst() {
        for (int i = 0; i < termArrayList.size(); i++) {
            if (termArrayList.get(i).getFactorArrayList().size() == 1 &&
                    termArrayList.get(i).getFactorArrayList().get(0) instanceof Pow) {
                return i;
            } else if (termArrayList.get(i).getFactorArrayList().size() == 1 &&
                    ((Const) termArrayList.get(i).getFactorArrayList().get(0)).
                            getNum().compareTo(BigInteger.ZERO) > 0) {
                return i;
            } else {
                for (Factor factor : termArrayList.get(i).getFactorArrayList()) {
                    if (factor instanceof Const &&
                            ((Const) factor).getNum().compareTo(BigInteger.ZERO) > 0) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        if (termArrayList.isEmpty()) {
            return "0";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int start = getFirst();
        if (start != -1) {
            stringBuffer.append(termArrayList.get(start).toString());
        }
        for (int i = 0; i < termArrayList.size(); i++) {
            if (i == start) {
                continue;
            }
            stringBuffer.append(termArrayList.get(i).toString());
        }
        if (start != -1) {
            return stringBuffer.substring(1);
        }
        return stringBuffer.toString();
    }
}
