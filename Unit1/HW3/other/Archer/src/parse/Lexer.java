package parse;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    private String getPower() {
        StringBuilder sb = new StringBuilder();
        sb.append(input.charAt(pos));
        pos += 1;
        if ("*".indexOf(input.charAt(pos)) != -1) {
            sb.append(input.charAt(pos));
            pos += 1;
        }

        return sb.toString();
    }

    private String getFunc() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(input.charAt(pos));
            pos += 1;
        }

        return sb.toString();
    }

    public void next() {
        while (pos < input.length() && " \t".indexOf(input.charAt(pos)) != -1) {
            ++pos;
        }

        if (pos >= input.length()) {
            return;
        }

        char c = input.charAt(pos);

        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("cs".indexOf(c) != -1) {
            curToken = getFunc();
        } else if ("fghiuxyz".indexOf(c) != -1) {
            curToken = String.valueOf(c);
            pos += 1;
        } else if ("*".indexOf(c) != -1) {
            curToken = getPower();
        } else if ("()+-,".indexOf(c) != -1) {
            curToken = String.valueOf(c);
            pos += 1;
        }
    }

    public String peek() {
        return this.curToken;
    }
}
