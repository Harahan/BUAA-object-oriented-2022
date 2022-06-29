public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;
    private String nextcurToken;

    public String getInput() {
        return input;
    }

    public int getPos() {
        return pos;
    }

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
        //System.out.println(c);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("()+*-".indexOf(c) != -1) {
            pos += 1;
            curToken = String.valueOf(c);
        } else {
            pos += 1;
            curToken = String.valueOf('x');
        }
        //System.out.println(curToken);
    }

    public String peek() {
        return this.curToken;
    }

    public String nextPeek() {
        return String.valueOf(this.input.charAt(this.pos));
    }
}
