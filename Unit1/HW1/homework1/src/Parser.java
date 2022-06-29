import expr.Const;
import expr.Pow;
import expr.Term;
import expr.Expr;
import expr.Factor;

import java.math.BigInteger;
import java.util.regex.Pattern;

public class Parser {
    private Lexer lexer;
    private static final Pattern PATTERNCONST = Pattern.compile("\\d+");
    private static final Pattern PATTERNPOW = Pattern.compile("x\\*\\*[\\+]?\\d+");
    private static final Pattern PATTERNADDSUB = Pattern.compile("[\\+\\-]");
    private static final Pattern PATTERNBRACKETS = Pattern.compile("\\)\\*\\*[\\+]?\\d+");

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() throws Exception { // Term must have 1 or -1
        Expr expr = new Expr();
        Term term;
        String addSub;
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) { // + -> 1 - -> 0
            addSub = lexer.peek();
            lexer.next();
            term = parseTerm();
            term.addFactor(new Const(new BigInteger(addSub + "1")));
            term.toExprBrackets();
            expr.addTerm(term);
        } else {
            term = parseTerm();
            term.addFactor(new Const(new BigInteger("1")));
            term.toExprBrackets();
            expr.addTerm(term);
        }
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            addSub = lexer.peek();
            lexer.next();
            term = parseTerm();
            term.addFactor(new Const(new BigInteger(addSub + "1")));
            term.toExprBrackets();
            expr.addTerm(term);
        }
        return expr;
    }

    public Term parseTerm() throws Exception {
        Term term = new Term();
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) { // term begin with + or -
            BigInteger tmp = new BigInteger(lexer.peek() + "1");
            term.addFactor(new Const(tmp));
            lexer.next();
        }
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() throws Exception {
        if (lexer.peek().equals("(")) { // (expr) ** 2 or (expr) ** +2 or (expr)
            lexer.next();
            Expr expr = parseExpr();

            if (PATTERNBRACKETS.matcher(lexer.peek()).matches()) {
                String bracketsRight = lexer.peek();
                lexer.next();
                return new Expr(expr, bracketsRight);
            } else {
                System.out.println("Wrong Format!");
                System.exit(0);
            }

        } else if (PATTERNCONST.matcher(lexer.peek()).matches()) { // 2
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Const(num);
        } else if (PATTERNADDSUB.matcher(lexer.peek()).matches()) { // +2 or -2
            String addSub = lexer.peek();
            int pos1 = lexer.getPos();
            lexer.consumeWhite();
            if (lexer.getPos() - pos1 != 0) {
                System.out.println("Wrong Format!");
                System.exit(0);
            }
            lexer.next();
            if (PATTERNCONST.matcher(lexer.peek()).matches()) {
                BigInteger num = new BigInteger(addSub + lexer.peek());
                lexer.next();
                return new Const(num);
            } else {
                System.out.println("Wrong Format!");
                System.exit(0);
            }
            //- - x **+2  *  x **+3
        } else if (PATTERNPOW.matcher(lexer.peek()).matches()) { // x ** 2 or x ** +2 or x
            String pow = lexer.peek();
            lexer.next();
            return new Pow(pow);
        } else {
            System.out.println("Wrong Format!");
            System.exit(0);
        }
        return null;
    }
}
