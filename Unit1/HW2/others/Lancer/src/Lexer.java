public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken = "+";
    private String excurToken = "+";
    private String exexcurToken = "+";

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && (Character.isDigit(input.charAt(pos))
                || Character.toString(input.charAt(pos)).equals("x"))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    private String getTrigo() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && !Character.toString(input.charAt(pos)).equals(")")) {
            sb.append(input.charAt(pos));
            ++pos;
        }
        sb.append(input.charAt(pos));
        ++pos;
        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c) || Character.toString(c).equals("x")) {
            exexcurToken = excurToken;
            excurToken = curToken;
            curToken = getNumber();
        } else if (Character.toString(c).equals("c") || Character.toString(c).equals("s")) {
            exexcurToken = excurToken;
            excurToken = curToken;
            curToken = getTrigo();
        } else if ("()+-*^".indexOf(c) != -1) {
            pos += 1;
            exexcurToken = excurToken;
            excurToken = curToken;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }

    public String peekex() {
        return this.excurToken;
    }

    public String peekexex() {
        return this.exexcurToken;
    }
}