package expr;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function implements Factor {
    private BigInteger index = BigInteger.ONE;
    private static final Pattern INDEX = Pattern.compile("[+]?\\d+");

    @Override
    public Factor getBase() {
        return null;
    }

    public void setIndex(String bracketsRight) {
        Matcher matcher = INDEX.matcher(bracketsRight);
        if (matcher.find()) {
            index = new BigInteger(matcher.group());
        }
    }

    public BigInteger getIndex() {
        return index;
    }

    public Factor calculate(BigInteger index) {
        this.index = this.index.multiply(index);
        return this;
    }
}
