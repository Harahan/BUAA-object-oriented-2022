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
            sb.append(input.charAt(pos));//
            ++pos;
        }

        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }

        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = removeZero(getNumber());//
        } else if (c == '(' || c == '+' || c == '*' || c == ')' || c == '-') {
            pos += 1;
            curToken = String.valueOf(c);
        } else if (Character.isLetter(c)) {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public char nextChar() {
        return input.charAt(pos + 1);
    }

    public String peek() {
        return this.curToken;
    }

    public static String removeZero(String str) {
        int len = str.length();
        int i = 0;
        while (i < len && str.charAt(i) == '0') {
            i++;
        }
        if (i == len) {
            return "0";
        }
        return str.substring(i);
    }
}
