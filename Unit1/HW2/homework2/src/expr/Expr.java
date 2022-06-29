package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expr implements Factor {
    private ArrayList<Term> termArrayList = new ArrayList<>();
    private BigInteger index = new BigInteger("1");
    private static final Pattern INDEX = Pattern.compile("[+]?\\d+");

    public BigInteger getIndex() {
        return index;
    }

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
        Matcher matcher = INDEX.matcher(bracketsRight);
        if (matcher.find()) {
            index = new BigInteger(matcher.group());
        }
    }

    public ArrayList<Term> getTermArrayList() {
        return termArrayList;
    }

    public void addTerm(Term term) {
        termArrayList.add(term);
    }

    /** (Expr)res -> (many Term) Term(const | const, pow) */
    public Expr mulExpr(Expr other) {
        Expr res = new Expr();
        for (Term myTerm : termArrayList) {
            for (Term otherTerm : other.termArrayList) {
                res.termArrayList.addAll(((Expr) myTerm.mulTerm(otherTerm).
                        getFactorArrayList().get(0)).termArrayList);
            }
        }
        res = res.simplify();
        return res;
    }

    /** (termArrayList.get(i).expand()) -> Term
     *  (1)Term -> (1)Expr -> (many)Term(const, pow, sin, cos)
     *  res: Expr -> term(const, pow, sin..., cos...)
     */
    public Expr expand() {
        Expr res = new Expr();
        for (int i = 0; i < termArrayList.size(); i++) {
            res.getTermArrayList().
                    addAll(((Expr) termArrayList.get(i).expand().
                            getFactorArrayList().get(0)).termArrayList);
        }
        res = res.simplify();
        return res;
    }

    public Expr expandIndex() {
        ArrayList<Factor> factors = new ArrayList<>();
        for (int i = 1; i <= index.intValue(); i++) {
            factors.add(new Expr(termArrayList, BigInteger.ONE));
        }
        Term term = new Term();
        term.setFactorArrayList(factors);
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(term);
        return new Expr(terms, BigInteger.ONE);
    }

    public Expr simplify() {
        HashMap<HashSet<Factor>, BigInteger> hashMap = new HashMap<>();
        for (Term term : getTermArrayList()) {
            HashSet<Factor> hashSet = new HashSet<>(term.getFactorArrayList());
            if (hashMap.containsKey(hashSet)) {
                hashMap.put(hashSet, hashMap.get(hashSet).add(term.getCoefficient()));
            } else {
                hashMap.put(hashSet, term.getCoefficient());
            }
        }
        ArrayList<Term> terms = new ArrayList<>();
        hashMap.forEach((k, v) -> terms.add(new Term(v, k)));
        terms.removeIf(term -> term.getCoefficient().equals(BigInteger.ZERO)); /*remove*/
        return new Expr(terms, BigInteger.ONE);
    }

    public int getFirst() {
        int start = -1;
        for (Term term : termArrayList) {
            if (term.getCoefficient().compareTo(BigInteger.ZERO) > 0) {
                start =  termArrayList.indexOf(term);
                break;
            }
        }
        return start;
    }

    @Override
    public String toString() {
        if (termArrayList.size() == 0) {
            return "0";
        }
        if (index.equals(BigInteger.ZERO)) {
            return "1";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int start = getFirst();
        if (start != -1) {
            stringBuffer.append(termArrayList.get(start).toString());
        }
        for (int i = 0; i < termArrayList.size(); i++) {
            if (i != start) {
                stringBuffer.append(termArrayList.get(i).toString());
            }
        }
        if (stringBuffer.toString().charAt(0) == '-') {
            stringBuffer = new StringBuffer("+" + stringBuffer);
        }
        if (!index.equals(BigInteger.ONE)) {
            return "(" + stringBuffer.substring(1) + ")" + "**" + index;
        }
        return "(" + stringBuffer.substring(1) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expr expr = (Expr) o;
        return Objects.equals(termArrayList, expr.termArrayList)
                && Objects.equals(index, expr.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(termArrayList, index);
    }

    @Override
    public Factor getBase() {
        return this;
    }
}
