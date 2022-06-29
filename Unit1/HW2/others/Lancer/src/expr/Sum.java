package expr;

import java.math.BigInteger;

public class Sum {
    private final String str;
    private BigInteger begin;
    private BigInteger end;
    private String fun;
    private String simplify;

    public Sum(String str) {
        this.str = str.replaceAll("sin", "@");
        String[] temp = this.str.split(",");
        this.begin = new BigInteger(temp[1]);
        this.end = new BigInteger(temp[2]);
        this.fun = temp[3].substring(0, temp[3].length() - 1);
        if (this.begin.compareTo(this.end) > 0) {
            this.simplify = "0";
        } else if (!this.fun.contains("i")) {
            this.simplify = "(" + this.end.subtract(this.begin).add(BigInteger.valueOf(1))
                    + "*" + this.fun.replaceAll("@", "sin") + ")";
        } else {
            StringBuilder simplifying = new StringBuilder();
            for (BigInteger j = this.begin; j.compareTo(this.end) <= 0;
                 j = j.add(BigInteger.valueOf(1))) {
                simplifying.append("+").append(this.fun.replaceAll("i", "(" + j + ")"));
            }
            simplifying.delete(0, 1);
            String simp = simplifying.toString().replaceAll("@", "sin");
            while (simp.contains("sin((") || simp.contains("cos((")) {
                int begin = simp.contains("sin((") ? simp.indexOf("sin((") : simp.indexOf("cos((");
                int end = simp.indexOf(")", begin);
                StringBuilder refun1 = new StringBuilder(simp);
                refun1.delete(end, end + 1);
                refun1.delete(begin + 3, begin + 4);
                simp = String.valueOf(refun1);
            }
            this.simplify = "(" + simp + ")";
        }

    }

    public String getSimplify() {
        return simplify;
    }
}
