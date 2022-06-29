
public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        String in = input;
        in = in.replaceAll(" ","");
        in = in.replaceAll("\t","");
        in = in.replaceAll("\\+-","-");
        in = in.replaceAll("-\\+","-");
        in = in.replaceAll("\\+\\+","+");
        in = in.replaceAll("--","+");
        in = in.replaceAll("\\+-","-");
        in = in.replaceAll("\\+-","-");
        in = in.replaceAll("-\\+","-");
        in = in.replaceAll("\\+\\+","+");
        in = in.replaceAll("--","+");
        in = in.replaceAll("\\+-","-");
        this.input = in;
        this.next();
    }

    private boolean is = true;

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
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("()+*-x".indexOf(c) != -1) {
            pos += 1;
            char d = ' ';
            if (pos < input.length()) {
                d = input.charAt(pos);
            }
            if (c == '*' && d == '*') {
                curToken = "**";
                pos += 1;
            } else if ((c == '+' || c == '-') && (Character.isDigit(d) || d == 'x') && pos == 1)
            {
                curToken = String.valueOf(c);
                is = false;
            } else {
                curToken = String.valueOf(c);
            }
        }
    }

    public Boolean is() {
        return this.is;
    }

    public String peek() {
        return this.curToken;
    }

    public String remaining() {
        return input.substring(pos);
    }

}
