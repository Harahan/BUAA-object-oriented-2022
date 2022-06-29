package expr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

public class NumberandX implements Factor {
    private final String numandx;
    private final int power;
    private final boolean positive;

    public NumberandX(String numandx, int power, boolean positive) {
        this.numandx = numandx;
        this.power = power;
        this.positive = positive;
    }

    /*public ArrayList<BigInteger> calculatecoe() {
        ArrayList<BigInteger> var = new ArrayList<>();
        BigInteger fuhao = BigInteger.valueOf(positive ? 1 : -1);
        if (Objects.equals(this.numandx, "x")) {
            var.add(BigInteger.valueOf(1));
        } else {
            var.add(new BigInteger(numandx));
        }
        var.set(0, var.get(0).multiply(fuhao));
        return var;
    }

    public ArrayList<Integer> calculateexp() {
        ArrayList<Integer> var = new ArrayList<>();
        var.add(this.power);
        return var;
    }*/

    public HashMap<HashMap<String, Integer>, BigInteger> calculate() {
        HashMap<String, Integer> var = new HashMap<>();
        if (Objects.equals(this.numandx, "x")) {
            var.put("x", power);
            HashMap<HashMap<String, Integer>, BigInteger> var1 = new HashMap<>();
            var1.put(var, BigInteger.valueOf((positive ? 1 : -1)));
            return var1;
        } else {
            var.put("x", 0);
            HashMap<HashMap<String, Integer>, BigInteger> var1 = new HashMap<>();
            var1.put(var, new BigInteger(numandx).multiply(
                    BigInteger.valueOf((positive ? 1 : -1))));
            return var1;
        }
    }

}
