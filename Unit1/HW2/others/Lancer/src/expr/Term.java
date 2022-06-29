package expr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

public class Term {
    private final HashSet<Factor> factors;

    public Term() {
        this.factors = new HashSet<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    /* public BigInteger[] calculate(){
        BigInteger[] var = new BigInteger[2];
        for (Factor i : factors) {
//            int[] temp = new int[2];
//            temp = i.calculate();  // 数组赋值?
            var[0].add(BigInteger.valueOf(temp[0]));
            var[1].add(BigInteger.valueOf(temp[1]));
        }
        return var;
    }*/

    /*public ArrayList<BigInteger> calculatecoe() {
        ArrayList<BigInteger> var = new ArrayList<>();
        var.add(BigInteger.valueOf(1));
        for (Factor i : factors) {
            ArrayList<BigInteger> var1 = new ArrayList<>();
            for (BigInteger j : var) {
                for (BigInteger k : i.calculatecoe()) {
                    var1.add(j.multiply(k));
                }
            }
            var.clear();
            var.addAll(var1);
        }
        return var;
    }

    public ArrayList<Integer> calculateexp() {
        ArrayList<Integer> var = new ArrayList<>();
        var.add(0);
        for (Factor i : factors) {
            ArrayList<Integer> var1 = new ArrayList<>();
            for (int j : var) {
                for (int k : i.calculateexp()) {
                    var1.add(j + k);
                }
            }
            var.clear();
            var.addAll(var1);
        }
        return var;
    }*/
    public HashMap<HashMap<String, Integer>, BigInteger> calculate() {
        HashMap<String, Integer> var = new HashMap<>();
        var.put("x", 0);
        HashMap<HashMap<String, Integer>, BigInteger> var1 = new HashMap<>();
        var1.put(var, BigInteger.valueOf(1));

        for (Factor i : factors) {
            HashMap<HashMap<String, Integer>, BigInteger> var2 = new HashMap<>();
            for (HashMap<String, Integer> j : var1.keySet()) {
                for (HashMap<String, Integer> k : i.calculate().keySet()) {
                    BigInteger bigInteger;
                    HashMap<String, Integer> temp = new HashMap<>(j);
                    for (String m : k.keySet()) {
                        if (temp.containsKey(m)) {
                            temp.put(m, temp.get(m) + k.get(m));
                        } else {
                            temp.put(m, k.get(m));
                        }
                    }
                    bigInteger = var1.get(j).multiply(i.calculate().get(k));
                    if (var2.containsKey(temp)) {
                        var2.put(temp, var2.get(temp).add(bigInteger));
                    } else {
                        var2.put(temp, bigInteger);
                    }
                }
            }
            var1.clear();
            var1.putAll(var2);
        }
        return var1;
    }
}
