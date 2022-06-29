import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;
    
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    public Expr parseExpr(boolean neg) {
        Expr expr = new Expr();
        Term term = parseTerm(neg);
        expr.addTerm(term);
        
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            //lexer.next();
            expr.addTerm(parseTerm(neg));
        }
        return expr;
    }
    
    public Term parseTerm(boolean neg) {
        Term term = new Term();
        Factor factor = parseFactor(neg);
        term.addFactor(factor);
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor(false)); //仅首个因子决定是否取反
        }
        return term;
    }
    
    public Factor parseFactor(boolean neg) { //negative?
        String curToken = lexer.peek();
        if (curToken.equals("-")) {
            lexer.next();
            return parseFactor(!neg);
        }
        if (curToken.equals("+")) {
            lexer.next();
            return parseFactor(neg);
        }
        if (curToken.equals("(")) {
            lexer.next();
            Factor expr = parseExpr(false);
            lexer.next();
            curToken = lexer.peek();
            if (curToken.equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                BigInteger index = new BigInteger(lexer.peek());
                Factor exprPow = new Term(index,expr);
                lexer.next();
                expr = exprPow;
            }
            if (neg) {
                Term negExpr = new Term();
                negExpr.addFactor(new Number(BigInteger.valueOf(-1)));
                negExpr.addFactor(expr);
                return negExpr;
            }
            return expr;
        } else if (curToken.equals("x")) {
            lexer.next();
            curToken = lexer.peek();
            if (curToken.equals("^")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                BigInteger index = new BigInteger(lexer.peek());
                Factor powerFunc = new PowerFunc(neg,index);
                lexer.next();
                return powerFunc;
            } else {
                Factor powerFunc = new PowerFunc(neg,BigInteger.ONE);
                return powerFunc;
            }
        } else if (curToken.equals("s") || curToken.equals("c")) {
            return parseTriangleFunc(neg);
        } else {
            BigInteger num = new BigInteger(lexer.peek());
            if (neg) {
                num = num.negate();
            }
            lexer.next();
            return new Number(num);
        }
    }
    
    public Factor parseTriangleFunc(boolean neg) {
        String curToken = lexer.peek();
        final String type = curToken.equals("s") ? "sin" : "cos";
        lexer.next();
        lexer.next();
        lexer.next();
        lexer.next();
        String body = lexer.peek();
        int parenthesesCnt = 1;
        if (lexer.peek().equals("(")) { parenthesesCnt += 1; }
        while (parenthesesCnt > 0) {
            lexer.next();
            body = body + lexer.peek();
            if (lexer.peek().equals("(")) { parenthesesCnt += 1; }
            else if (lexer.peek().equals(")")) { parenthesesCnt -= 1; }
        }
        body = body.substring(0,body.length() - 1);
        lexer.next();
        curToken = lexer.peek();
        BigInteger index = BigInteger.ONE;
        if (curToken.equals("^")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            index = new BigInteger(lexer.peek());
            lexer.next();
        }
        return new TriangleFunc(type,body,index,neg);
    }
}
