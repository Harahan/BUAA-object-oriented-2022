package expr;

import java.math.BigInteger;

public class Sum extends Function {
    private final BigInteger begin;
    private final BigInteger end;
    private final String input;

    public Sum(BigInteger begin, BigInteger end, String input) {
        this.begin = begin;
        this.end = end;
        this.input = input;
    }

    public String expand() {
        StringBuilder res = new StringBuilder();
        if (begin.compareTo(end) > 0) {
            return "0";
        }
        for (BigInteger i = begin; i.compareTo(end) <= 0; i = i.add(BigInteger.ONE)) {
            String tmp = input;
            tmp = tmp.replaceAll("(?<!s)i",   "(" + i + ")");
            res.append("+(").append(tmp).append(")");
        }
        return res.substring(1);
    }
}
