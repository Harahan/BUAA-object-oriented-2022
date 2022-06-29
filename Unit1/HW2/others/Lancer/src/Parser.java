import expr.Expr;
import expr.Factor;
import expr.NumberandX;
import expr.Term;
import expr.Trigo;

import java.util.Objects;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr(!Objects.equals(lexer.peekexex(), "-"));
        expr.addTerm(parseTerm());

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            lexer.next();
            expr.addTerm(parseTerm());
        }
        lexer.next();
        if (lexer.peek().equals("^")) {
            lexer.next();
            int temp = Integer.parseInt(lexer.peek());
            lexer.next();
            expr.setPower(temp);
        }

        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("-") || lexer.peek().equals("+")) {
            lexer.next();
        }
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            //lexer.next();
            return expr;
        } else if (lexer.peek().charAt(0) == 's' || lexer.peek().charAt(0) == 'c') {
            /*lexer.next();
            if(lexer.peek().equals("i") || lexer.peek().equals("o")){
            }*/
            String str = lexer.peek();
            boolean positive = !lexer.peekex().equals("-");
            lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                int temp = Integer.parseInt(lexer.peek());
                lexer.next();
                return new Trigo(str, temp, positive);
            } else {
                return new Trigo(str, 1, positive);
            }

        }
        /*else if(lexer.peek().equals("f") || lexer.peek().equals("g") || lexer.peek().equals("h")){
        }*/
        else {
            String numandx = lexer.peek();
            String fuhao = lexer.peekex();
            boolean positive = !Objects.equals(fuhao, "-");
            if (!Objects.equals(numandx, "x")) {
                lexer.next();
                return new NumberandX(numandx, 0, positive);
            }
            lexer.next();
            if (lexer.peek().equals("^")) {
                lexer.next();
                int temp = Integer.parseInt(lexer.peek());
                lexer.next();
                return new NumberandX(numandx, temp, positive);
            } else {
                return new NumberandX(numandx, 1, positive);
            }
        }
    }
}
