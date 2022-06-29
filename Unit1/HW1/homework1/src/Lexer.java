public class Lexer {
    private String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) throws Exception {
        this.input = input;
        next();
    }

    public int getPos() {
        return pos;
    }

    public String getNumber() {
        StringBuffer sb = new StringBuffer();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    public void consumeWhite() {
        while (pos < input.length() && " \t".indexOf(input.charAt(pos)) != -1) {
            pos++;
        }
    }

    public void pow() {
        pos += 1;
        consumeWhite();
        if (pos + 1 < input.length() && "*".indexOf(input.charAt(pos)) != -1
                && "*".indexOf(input.charAt(pos + 1)) != -1) { // x ** 2 or x ** +2
            pos += 2;
            consumeWhite();
            if (Character.isDigit(input.charAt(pos))) { // x ** 2
                curToken = "x**" + getNumber();
            } else if ("+".indexOf(input.charAt(pos)) != -1 &&
                    Character.isDigit(input.charAt(pos + 1))) { // x ** +2
                pos += 1;
                curToken = "x**" + getNumber();
            } else {
                System.out.println("Wrong Format!");
                System.exit(0);
            }
        } else if (pos == input.length() || "*+-)".indexOf(input.charAt(pos)) != -1) { // x
            curToken = "x**1";
        } else {
            System.out.println("Wrong Format!");
            System.exit(0);
        }
    }

    public void bracketRight() {
        pos += 1;
        consumeWhite();
        if (pos + 1 < input.length() && "*".indexOf(input.charAt(pos)) != -1
                && "*".indexOf(input.charAt(pos + 1)) != -1) {
            pos += 2;
            consumeWhite();
            if (Character.isDigit(input.charAt(pos))) { // ) ** 2
                curToken = ")**" + getNumber();
            } else if ("+".indexOf(input.charAt(pos)) != -1 &&
                    Character.isDigit(input.charAt(pos + 1))) { // ) ** +2
                pos += 1;
                curToken = ")**" + getNumber();
            } else {
                System.out.println("Wrong Format!");
                System.exit(0);
            }
        } else if (pos == input.length() || "*+-)".indexOf(input.charAt(pos)) != -1) { // )
            curToken = ")**1";
        } else {
            System.out.println("Wrong Format!");
            System.exit(0);
        }
    }

    public void next() {
        consumeWhite(); // eat space and tab
        if (pos == input.length()) {
            curToken = "END";
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) { // 2
            curToken = getNumber();
        } else if ("x".indexOf(c) != -1) { // x ** 2 or x ** +2 or x
            pow();
        } else if ("(+-*".indexOf(c) != -1) { // * or + or - or (
            pos += 1;
            curToken = String.valueOf(c);
        } else if (")".indexOf(c) != -1) { // ) or ) ** +2 or ) ** 2
            bracketRight();
        } else {
            System.out.println("Wrong Format!");
            System.exit(0);
        }
    }

    public String peek() {
        return this.curToken;
    }
}
