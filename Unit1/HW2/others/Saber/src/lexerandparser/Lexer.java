package lexerandparser;

public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        get();
    }

    public void get() {
        if (pos == input.length()) {
            return;
        } else if (Character.isDigit(input.charAt(pos))) {
            getNum();
        } else if ("-+*^x()i,".indexOf(input.charAt(pos)) != -1) {
            curToken = String.valueOf(input.charAt(pos));
            pos++;
        } else if ("sfghcS".indexOf(input.charAt(pos)) != -1) {
            getTriFuncSum();
        }
    }

    public void getNum() {
        int i = pos;
        StringBuilder sb = new StringBuilder();
        while (i < input.length() && Character.isDigit(input.charAt(i))) {
            sb.append(input.charAt(i));
            pos++;
            i++;
        }
        curToken = sb.toString();
    }

    public void getTriFuncSum() {
        if ("fgh".indexOf(input.charAt(pos)) != -1) {
            curToken = input.substring(pos, pos + 1);
            pos++;
        } else {
            curToken = input.substring(pos, pos + 3);
            pos += 3;
        }
    }

    public String peek() {
        return curToken;
    }
}
