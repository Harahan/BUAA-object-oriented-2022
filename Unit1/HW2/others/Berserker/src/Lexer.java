
public class Lexer {
    private final String input;
    private int pos = 0;
    private int pastpos = 0;
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

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        pastpos = pos;
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else {
            if (c == 's' || c == 'c') {
                pos += 4;
            } else {
                pos += 1;
            }
            curToken = String.valueOf(c);
        }
    }

    public void last() {
        pos = pastpos;
        curToken = "*";
    }

    public String peek() {
        return curToken;
    }
}
