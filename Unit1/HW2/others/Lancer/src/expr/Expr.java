package expr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Expr implements Factor {
    private final HashSet<Term> terms;
    private int power = 1;
    private final boolean positive;

    public Expr(boolean positive) {
        this.terms = new HashSet<>();
        this.positive = positive;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public HashMap<HashMap<String, Integer>, BigInteger> calculate() {
        HashMap<String, Integer> var = new HashMap<>();
        HashMap<HashMap<String, Integer>, BigInteger> var1 = new HashMap<>();
        var.put("x", 0);
        var1.put(var, BigInteger.valueOf(0));
        if (this.power == 0) {
            var1.put(var, BigInteger.valueOf(1));
        } else {
            for (Term i : terms) {
                for (HashMap<String, Integer> k : i.calculate().keySet()) {
                    int flag = 0;
                    HashMap<String, Integer> j1 = null;
                    for (HashMap<String, Integer> j : var1.keySet()) {
                        int temp = 0;
                        if (j.size() == k.size()) {
                            for (String m : k.keySet()) {
                                temp++;
                                if (!j.containsKey(m) || (j.containsKey(m)
                                        && !Objects.equals(j.get(m), k.get(m)))) {
                                    break; }
                                if (temp == k.size()) {
                                    flag = 1;
                                    j1 = j; } } } }
                    if (flag == 1) {
                        //System.out.println("@");
                        var1.put(k, i.calculate().get(k).add(var1.get(j1)));
                    } else {
                        var1.put(k, i.calculate().get(k)); } } }
            //处理power
            var.clear();
            var.put("x", 0);
            HashMap<HashMap<String, Integer>, BigInteger> var3 = new HashMap<>();
            var3.put(var, BigInteger.valueOf(1));
            for (int i = 1; i <= power; i++) {
                HashMap<HashMap<String, Integer>, BigInteger> var2 = new HashMap<>();
                for (HashMap<String, Integer> j : var3.keySet()) {
                    for (HashMap<String, Integer> k : var1.keySet()) {
                        BigInteger bigInteger;
                        HashMap<String, Integer> temp = new HashMap<>(j);
                        for (String m : k.keySet()) {
                            if (temp.containsKey(m)) {
                                temp.put(m, temp.get(m) + k.get(m));
                            } else {
                                temp.put(m, k.get(m));
                            }
                        }
                        bigInteger = var3.get(j).multiply(var1.get(k));
                        if (var2.containsKey(temp)) {
                            var2.put(temp, var2.get(temp).add(bigInteger));
                        } else {
                            var2.put(temp, bigInteger); } } }
                var3.clear();
                var3.putAll(var2);
            }
            var1.clear();
            var1.putAll(var3);
        }
        //处理符号
        for (HashMap<String, Integer> i : var1.keySet()) {
            var1.replace(i, var1.get(i).multiply(BigInteger.valueOf(this.positive ? 1 : -1)));
        }
        return var1;
    }

    /*public ArrayList<BigInteger> calculatecoe() {
        ArrayList<BigInteger> var = new ArrayList<>();
        for (Term i : terms) {
            var.addAll(i.calculatecoe());
        }
        //处理乘方
        ArrayList<BigInteger> var1 = new ArrayList<>();
        var1.addAll(var);
        for (int i = 1; i < power; i++) {
            ArrayList<BigInteger> var2 = new ArrayList<>();
            for (BigInteger j : var) {
                for (BigInteger k : var1) {
                    var2.add(j.multiply(k));
                }
            }
            var.clear();
            var.addAll(var2);
        }
        if (power == 0) {
            var.clear();
            var.add(BigInteger.valueOf(1));
        }
        BigInteger fuhao = BigInteger.valueOf(positive ? 1 : -1);
        for (int i = 0; i < var.size(); i++) {
            var.set(i, var.get(i).multiply(fuhao));
        }
        return var;
    }

    public ArrayList<Integer> calculateexp() {
        ArrayList<Integer> var = new ArrayList<>();
        for (Term i : terms) {
            var.addAll(i.calculateexp());
        }
        ArrayList<Integer> var1 = new ArrayList<>();
        var1.addAll(var);
        for (int i = 1; i < power; i++) {
            ArrayList<Integer> var2 = new ArrayList<>();
            for (int j : var) {
                for (int k : var1) {
                    var2.add(j + k);
                }
            }
            var.clear();
            var.addAll(var2);
        }
        if (power == 0) {
            var.clear();
            var.add(0);
        }
        return var;
    }*/
    /*    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append(" ");
            sb.append(iter.next().toString());
            sb.append(" +");
            while (iter.hasNext()) {
                sb.append(" ");
                sb.append(iter.next().toString());
                sb.append(" +");
            }
        }
        return sb.toString();
    }*/
}
