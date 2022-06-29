import expr.Expr;
import expr.Factor;
import expr.Number;
import expr.Term;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        boolean isPos = true;
        while (lexer.peek().equals("-") || lexer.peek().equals("+")) {
            if (lexer.peek().equals("-")) {
                isPos = !isPos;
            }
            lexer.next();
        }
        expr.addTerm(parseTerm(isPos));
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            boolean newIsPos = true;
            while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
                if (lexer.peek().equals("-")) {
                    newIsPos = !newIsPos;
                }
                lexer.next();
            }
            expr.addTerm(parseTerm(newIsPos));
        }
        return expr;
    }

    public Term parseTerm(boolean isPos) {
        Term term = new Term();
        if (!isPos) {
            term.addFactor(new Number(new BigInteger("-1"),new BigInteger("0")));
        }
        Factor lastFactor = parseFactor(true);
        term.addFactor(lastFactor);

        while (lexer.peek().equals("*")) {
            lexer.next();
            if (lexer.peek().equals("*")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                String times = lexer.peek();
                int exp = Integer.valueOf(times).intValue();
                if (exp == 0) {
                    term.deleteFactor();//判0次方
                    term.addFactor(new Number(new BigInteger("1"),new BigInteger("0")));
                }
                for (int i = 0; i < exp - 1; i++) {
                    if (lastFactor instanceof Number) {
                        Number temp = new Number(((Number) lastFactor).abs(),
                                ((Number) lastFactor).getExp());
                        temp.abs();
                        term.addFactor(temp);
                    } else {
                        term.addFactor(lastFactor);
                    }
                }
                lexer.next();
            }
            else {
                lastFactor = parseFactor(true);
                term.addFactor(lastFactor);//
            }

        }
        return term;
    }

    public Factor parseFactor(boolean isPos) {
        boolean pos = isPos;
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("-")) {
                pos = !pos;
            }
            lexer.next();
        }
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();//
            return expr;
        } else if (Character.isLetter(lexer.peek().charAt(0))) {
            lexer.next();
            if (pos) {
                return new Number(new BigInteger("1"), new BigInteger("1"));
            }
            else {
                return new Number(new BigInteger("-1"), new BigInteger("1"));
            }
        } else {
            BigInteger num;
            if (pos) {
                num = new BigInteger(lexer.peek());
            } else {
                num = new BigInteger("-" + lexer.peek());
            }
            lexer.next();
            return new Number(num,new BigInteger("0"));
        }
    }
}
