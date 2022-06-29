package expr;

import java.math.BigInteger;
import java.util.HashMap;

public class Trigo implements Factor {
    private final String rawstr;
    private String str;
    private int power;
    private boolean positive;
    private int simp = 1;

    public Trigo(String rawstr, int power, boolean positive) {
        this.rawstr = rawstr;
        this.power = power;
        this.positive = positive;
        int a = rawstr.indexOf("(");
        int b = rawstr.indexOf(")");
        //处理括号中的部分
        String rawfun = rawstr.substring(a + 1, b);
        String fun;
        if (rawfun.contains("x")) {
            if (rawfun.contains("^")) {
                int c = rawfun.indexOf("^");
                int power1 = Integer.parseInt(rawfun.substring(c + 1));
                if (power1 == 0) {
                    fun = "1";
                } else if (power1 == 1) {
                    fun = "x";
                } else {
                    fun = "x^" + power1;
                }
            } else {
                fun = rawfun;
            }
            this.str = rawstr.replace(rawfun, fun);
        } else {
            int i = new BigInteger(rawfun).compareTo(BigInteger.valueOf(0));
            if (i == 0) {
                this.str = "x";
                this.power = 0;
                if (rawstr.contains("sin")) {
                    this.simp = 0;
                }
            } else if (i < 0) {
                fun = (new BigInteger(rawfun.substring(1))).toString();
                if (rawstr.contains("sin") && this.power % 2 == 1) {
                    this.positive = !positive;
                }
                this.str = rawstr.replace(rawfun, fun);
            } else {
                fun = (new BigInteger(rawfun)).toString();
                this.str = rawstr.replace(rawfun, fun);
            }
        }
    }

    @Override
    public HashMap<HashMap<String, Integer>, BigInteger> calculate() {
        HashMap<String, Integer> var = new HashMap<>();
        //System.out.print(rawstr);
        var.put(this.str, this.power);
        HashMap<HashMap<String, Integer>, BigInteger> var1 = new HashMap<>();
        var1.put(var, BigInteger.valueOf(positive ? 1 : -1).
                multiply(BigInteger.valueOf(this.simp)));
        return var1;
    }
}
