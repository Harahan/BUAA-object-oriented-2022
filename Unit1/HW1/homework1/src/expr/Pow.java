package expr;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pow implements Factor {
    private BigInteger index;
    private static final Pattern PATTERNINDEX = Pattern.compile("[+]?\\d+");

    public Pow(String pow) {
        Matcher matcher = PATTERNINDEX.matcher(pow);
        matcher.find();
        this.index = new BigInteger(matcher.group());
    }

    @Override
    public String toString() {
        if (index.toString().equals("0")) {
            return "1";
        } else if (index.toString().equals("1")) {
            return "x";
        } else if (index.toString().equals("2")) {
            return "x*x";
        }
        StringBuffer pow = new StringBuffer("x**");
        pow.append(index.toString());
        return pow.toString();
    }

    public BigInteger getIndex() {
        return index;
    }

}
