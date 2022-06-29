public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input.replaceAll("[\\t ]+", "");
        next();
    }

    public String getNumber() {
        StringBuffer sb = new StringBuffer();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    public String getPow() {
        StringBuffer sb = new StringBuffer();
        pos += 1;
        if (pos + 1 < input.length() && "*".indexOf(input.charAt(pos)) != -1
                && "*".indexOf(input.charAt(pos + 1)) != -1) { // x ** 2 or x ** +2
            pos += 2;
            if (Character.isDigit(input.charAt(pos))) { // x ** 2
                sb.append("x**").append(getNumber());
            } else { // x ** +2
                pos += 1;
                sb.append("x**").append(getNumber());
            }
        } else { // x
            sb.append("x**1");
        }
        return sb.toString();
    }

    public String getBracketRight() {
        StringBuffer sb = new StringBuffer();
        pos += 1;
        if (pos + 1 < input.length() && "*".indexOf(input.charAt(pos)) != -1
                && "*".indexOf(input.charAt(pos + 1)) != -1) {
            pos += 2;
            if (Character.isDigit(input.charAt(pos))) { // ) ** 2
                sb.append(")**").append(getNumber());
            } else { // ) ** +2
                pos += 1;
                sb.append(")**").append(getNumber());
            }
        } else { // )
            sb.append(")**1");
        }
        return sb.toString();
    }

    public String getSin() {
        StringBuffer sb = new StringBuffer("sin(");
        pos += 4;
        return sb.toString();
    }

    public String getCos() {
        StringBuffer sb = new StringBuffer("cos(");
        pos += 4;
        return sb.toString();
    }

    public String getSum() {
        StringBuffer sb = new StringBuffer("sum(i,");
        pos += 6;
        return sb.toString();
    }

    public String getDiyFunction() {
        StringBuffer sb = new StringBuffer(input.charAt(pos) + "(");
        pos += 2;
        return sb.toString();
    }

    /**
     * 1. unsigned const
     * 2. power
     * 3. times or plus or minus or left bracket or comma
     * 4. right bracket( + index)
     * 5. sin + left bracket
     * 6. cos + left bracket
     * 7. sum + left bracket + i,
     * 8. function name + left bracket
     * 9. i
     */

    public void next() {
        if (pos == input.length()) {
            curToken = "END";
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("x".indexOf(c) != -1) {
            curToken = getPow();
        } else if ("(+-*,i".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);
        } else if (")".indexOf(c) != -1) {
            curToken = getBracketRight();
        } else if ("s".indexOf(c) != -1 && "i".indexOf(input.charAt(pos + 1)) != -1) {
            curToken = getSin();
        } else if ("c".indexOf(c) != -1) {
            curToken = getCos();
        } else if ("s".indexOf(c) != -1 && "u".indexOf(input.charAt(pos + 1)) != -1) { // sum
            curToken = getSum();
        } else if ("fgh".indexOf(c) != -1) {
            curToken = getDiyFunction();
        }
    }

    public String peek() {
        return this.curToken;
    }
}
