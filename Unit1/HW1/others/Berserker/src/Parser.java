import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm(true));
        //System.out.println(lexer.peek());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            //System.out.println(lexer.getInput().substring(lexer.getPos()));
            if (lexer.peek().equals("+")) {
                lexer.next();
                expr.addTerm(parseTerm(true));
            }
            else {
                lexer.next();
                expr.addTerm(parseTerm(false));
            }
        }
        return expr;
    }

    public Term parseTerm(boolean value) {
        Term term = new Term(value);
        term.addFactor(parseFactor(value));
        //System.out.println( lexer.peek());
        while (lexer.peek().equals("*") && !lexer.nextPeek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor(value));
        }
        return term;
    }

    public Factor parseFactor(boolean value) {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Expr expr = this.parseExpr();
            lexer.next();
            int power = 1;
            if (lexer.peek().equals("*") && lexer.nextPeek().equals("*")) {
                lexer.next();
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                power = Integer.parseInt(lexer.peek());
                lexer.next();
            }
            expr.setPower(power);
            return expr;
        } else {
            if (lexer.peek().equals("x")) {
                lexer.next();
                BigInteger num = new BigInteger("1");
                int power = 1;
                if (lexer.peek().equals("*") && lexer.nextPeek().equals("*")) {
                    lexer.next();
                    lexer.next();
                    if (lexer.peek().equals("+")) {
                        lexer.next();
                    }
                    power = Integer.parseInt(lexer.peek());
                    lexer.next();
                }
                return new Variable(num,power);
            }
            else {
                int check = 0;
                if (lexer.peek().equals("-")) {
                    lexer.next();
                    check = 1;
                }
                else if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                BigInteger num = new BigInteger(lexer.peek());
                lexer.next();
                if (check == 1) {
                    num = num.negate();
                }
                return new Variable(num,0);
            }

        }
    }
}

