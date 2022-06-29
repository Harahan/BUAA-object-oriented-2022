package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Expr implements Factor {
    private ArrayList<Term> terms;

    public Expr() {
        terms = new ArrayList<>();
    }

    public Expr(Expr expr) {
        terms = expr.getTerms();
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void mulExpr(Expr expr) {
        ArrayList<Term> newterms = new ArrayList<>();
        ArrayList<Term> termjs = new ArrayList<>();
        for (Term term:expr.terms) {
            termjs.add(new Term(term));
        }
        for (Term termi:terms) {
            for (Term termj:termjs) {
                Term termk = new Term(termi.getIndex() + termj.getIndex(),
                        termi.getCoefficient().multiply(termj.getCoefficient()));
                ArrayList<Trigo> trigos = new ArrayList<>();
                trigos.addAll(termi.getTrigos());
                trigos.addAll(termj.getTrigos());
                termk.setTrigos(trigos);
                newterms.add(termk);
            }
        }
        terms = newterms;
    }

    public void mulVariable(Variable variable) {
        for (Term term:terms) {
            term.setIndex(term.getIndex() + variable.getIndex());
            term.setCoefficient(term.getCoefficient().multiply(variable.getCoefficient()));
        }
    }

    public void sortTerm() {
        ArrayList<Term> newterms = new ArrayList<>();
        for (Term term:terms) {
            Expr expr = new Expr();
            if (term.getSymbol()) {
                expr.addTerm(new Term(0, BigInteger.ONE));
            }
            else {
                expr.addTerm(new Term(0, new BigInteger("-1")));
            }
            for (Factor factor:term.getFactors()) {
                if (factor instanceof Expr) {
                    expr.mulExpr((Expr) factor);
                } else {
                    expr.mulVariable((Variable) factor);
                }
            }
            expr.setTrigos(term.getTrigos());
            newterms.addAll(expr.getTerms());
        }
        for (Term term:newterms) {
            for (Trigo trigo : term.getTrigos()) {
                if (trigo.getContent().getCoefficient().toString().charAt(0) == '-') {
                    if (trigo.getMode() == 1) {
                        term.setCoefficient(term.getCoefficient().negate());
                    }
                }
            }
        }
        for (Term term:newterms) {
            for (Trigo trigo : term.getTrigos()) {
                trigo.getContent().setCoefficient(trigo.getContent().getCoefficient().abs());
            }
        }
        terms = newterms;
    }

    public void addTerm(Term term) {
        terms.add(term);
    }

    public void setTrigos(ArrayList<Trigo> trigos) {
        for (Term term:terms) {
            ArrayList<Trigo> trigos1 = term.getTrigos();
            for (Trigo trigo:trigos) {
                Trigo trigo1 = new Trigo(trigo);
                trigos1.add(trigo1);
            }
            term.setTrigos(trigos1);
        }
    }

    public void merge() {
        for (Term term:terms) {
            term.mergeTrigo();
        }
        ArrayList<Term> newterms = new ArrayList<>();
        for (int i = 0;i < terms.size();i++) {
            if (terms.get(i).isMerge()) {
                for (int j = i + 1;j < terms.size();j++) {
                    if (terms.get(j).isMerge()) {
                        terms.get(i).merge(terms.get(j));
                    }
                }
                newterms.add(terms.get(i));
            }
        }
        for (Term term:newterms) {
            ArrayList<Trigo> rmtrigos = new ArrayList<>();
            for (Trigo trigo:term.getTrigos()) {
                if (trigo.getContent().getCoefficient().equals(BigInteger.ZERO)) {
                    if (trigo.getMode() == 1) {
                        term.setCoefficient(BigInteger.ZERO);
                    } else {
                        rmtrigos.add(trigo);
                    }
                }
            }
            for (Trigo trigo:rmtrigos) {
                term.getTrigos().remove(trigo);
            }
        }
        ArrayList<Term> newterms1 = new ArrayList<>();
        for (Term term:newterms) {
            if (!term.getCoefficient().equals(BigInteger.ZERO)) {
                newterms1.add(term);
            }
        }
        terms = newterms1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int flag = -1;
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).getCoefficient().toString().charAt(0) != '-') {
                sb.append(terms.get(i).toString());
                flag = i;
                break;
            }
        }
        if (flag == -1 && terms.size() == 0) {
            return "0";
        }
        if (flag > -1) {
            terms.remove(flag);
        }
        if (terms.size() > 0) {
            Iterator<Term> iter = terms.iterator();
            if (flag == -1) {
                sb.append(iter.next().toString());
            }
            if (iter.hasNext()) {
                Term item = iter.next();
                if (item.getCoefficient().toString().charAt(0) != '-') {
                    sb.append("+");
                }
                sb.append(item);
                while (iter.hasNext()) {
                    Term item1 = iter.next();
                    if (item1.getCoefficient().toString().charAt(0) != '-') {
                        sb.append("+");
                    }
                    sb.append(item1);
                }
            }
        }
        return sb.toString();
    }
}
