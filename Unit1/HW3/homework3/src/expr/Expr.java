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

    public Expr getCosExpr(Factor factor) {
        Cos cos = new Cos(factor, "2");
        Term term1 = new Term();
        term1.setCoefficient(BigInteger.valueOf(-1));
        term1.addFactor(cos);
        Term term2 = new Term();
        term2.addFactor(new Const(BigInteger.ONE));
        Expr expr = new Expr();
        expr.addTerm(term1);
        expr.addTerm(term2);
        return expr;
    }

    public Term substituteSin(Term term) {
        Sin sin;
        Term res = new Term();
        res.setCoefficient(term.getCoefficient());
        for (Factor fac : term.getFactorArrayList()) {
            if (fac instanceof Sin &&
                    ((Sin) fac).getIndex().compareTo(BigInteger.valueOf(2)) >= 0) {
                sin = new Sin(((Sin) fac).getFactor(),
                        ((Sin) fac).getIndex().add(BigInteger.valueOf(-2)).toString());
                res.addFactor(sin);
                res.addFactor(getCosExpr(((Sin) fac).getFactor()));
            } else {
                res.addFactor(fac);
            }
        }
        return res;
    }

    public Expr getSinExpr(Factor factor) {
        Sin sin = new Sin(factor, "2");
        Term term1 = new Term();
        term1.setCoefficient(BigInteger.valueOf(-1));
        term1.addFactor(sin);
        Term term2 = new Term();
        term2.addFactor(new Const(BigInteger.ONE));
        Expr expr = new Expr();
        expr.addTerm(term1);
        expr.addTerm(term2);
        return expr;
    }

    public Term substituteCos(Term term) {
        Cos cos;
        Term res = new Term();
        res.setCoefficient(term.getCoefficient());
        for (Factor fac : term.getFactorArrayList()) {
            if (fac instanceof Cos &&
                    ((Cos) fac).getIndex().compareTo(BigInteger.valueOf(2)) >= 0) {
                cos = new Cos(((Cos) fac).getFactor(),
                        ((Cos) fac).getIndex().add(BigInteger.valueOf(-2)).toString());
                res.addFactor(cos);
                res.addFactor(getSinExpr(((Cos) fac).getFactor()));
            } else {
                res.addFactor(fac);
            }
        }
        return res;
    }

    public Expr mergeSquare() {
        Expr expr1 = new Expr();
        Expr expr2 = new Expr();
        for (Term term : termArrayList) {
            expr1.addTerm(substituteSin(term));
        }
        for (Term term : termArrayList) {
            expr2.addTerm(substituteCos(term));
        }
        expr1 = expr1.expand();
        expr2 = expr2.expand();
        Expr res;
        if (expr1.toString().length() < expr2.toString().length()) {
            res = expr1;
        } else {
            res = expr2;
        }
        if (res.toString().length() > this.toString().length()) {
            res = this;
        }
        return res;
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
            return "(0)";
        }
        if (index.equals(BigInteger.ZERO)) {
            return "(1)";
        }
        StringBuilder sb = new StringBuilder();
        int start = getFirst();
        if (start != -1) {
            sb.append(termArrayList.get(start).toString());
        }
        for (int i = 0; i < termArrayList.size(); i++) {
            if (i != start) {
                sb.append(termArrayList.get(i).toString());
            }
        }
        if (sb.toString().charAt(0) == '-') {
            sb = new StringBuilder("+" + sb);
        }
        if (!index.equals(BigInteger.ONE)) {
            return "(" + sb.substring(1) + ")" + "**" + index;
        }
        return "(" + sb.substring(1) + ")";
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
