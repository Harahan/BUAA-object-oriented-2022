import expr.Const;
import expr.Expr;
import expr.Factor;
import expr.Term;
import expr.Sum;
import expr.Sin;
import expr.Cos;
import expr.Pow;
import expr.DiyFunction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Parser {
    private final Lexer lexer;
    private static final Pattern CONST = Pattern.compile("\\d+");
    private static final Pattern POW = Pattern.compile("x\\*\\*\\d+");
    private static final Pattern SIGN = Pattern.compile("[\\+\\-]");
    private static final Pattern SIN = Pattern.compile("sin\\(");
    private static final Pattern COS = Pattern.compile("cos\\(");
    private static final Pattern SUM = Pattern.compile("sum\\(i,");
    private static final Pattern DIYFUNCTION = Pattern.compile("[fgh]\\(");

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    private String getTermSign() {
        String sign;
        sign = lexer.peek();
        lexer.next();
        return sign;
    }

    private Term getTerm(String sign) {
        Term term;
        term = parseTerm();
        term.addFactor(new Const(new BigInteger(sign + "1")));
        return term;
    }

    public Factor getFactor() {
        lexer.next();
        return parseFactor();
    }

    public Factor getExpr() {
        lexer.next();
        Expr expr = parseExpr();
        String bracketsRight = lexer.peek();
        lexer.next();
        return new Expr(expr, bracketsRight).expandIndex();
    }

    public Factor getExpr(String str) {
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        return parser.parseExpr().expandIndex();
    }

    public Const getConst() {
        StringBuffer num = new StringBuffer(lexer.peek());
        lexer.next();
        if (CONST.matcher(lexer.peek()).matches()) {
            num.append(lexer.peek());
            lexer.next();
        }
        return new Const(new BigInteger(num.toString()));
    }

    public Factor getPow() {
        Pow pow = new Pow(lexer.peek());
        lexer.next();
        return pow;
    }

    public Factor getSin() {
        lexer.next();
        Factor factor = parseFactor();
        Sin sin = new Sin(factor, lexer.peek());
        lexer.next();
        return sin;
    }

    public Factor getCos() {
        lexer.next();
        Factor factor = parseFactor();
        Cos cos = new Cos(factor, lexer.peek());
        lexer.next();
        return cos;
    }

    public StringBuffer getSumFactor() {
        int l = 0;
        int r = 0;
        StringBuffer stringBuffer = new StringBuffer();
        while (l >= r) {
            if (lexer.peek().indexOf('(') != -1) {
                l += 1;
            } else if (lexer.peek().indexOf(')') != -1) {
                r += 1;
            }
            if (l >= r) {
                stringBuffer.append(lexer.peek());
            }
            lexer.next();
        }
        return stringBuffer;
    }

    public Factor getSum() {
        lexer.next();
        BigInteger begin = new BigInteger(parseFactor().toString());
        lexer.next();
        BigInteger end = new BigInteger(parseFactor().toString());
        lexer.next();
        StringBuffer stringBuffer = getSumFactor();
        Sum sum = new Sum(begin, end, stringBuffer.toString());
        String str = sum.expand();
        return getExpr(str);
    }

    public Factor getDiyFunction() {
        String type = lexer.peek().substring(0, 1);
        ArrayList<Factor> factors = new ArrayList<>();
        while (!lexer.peek().equals(")**1")) {
            lexer.next();
            Factor factor = parseFactor();
            factors.add(factor);
        }
        lexer.next();
        DiyFunction diyFunction = new DiyFunction();
        String str = diyFunction.substituteInto(type, factors);
        return getExpr(str);
    }

    /** Every term has a factor const(1) or const(-1)*/
    public Expr parseExpr() {
        Expr expr = new Expr();
        String sign = "+";
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            sign = getTermSign();
        }
        expr.addTerm(getTerm(sign));
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            sign = getTermSign();
            expr.addTerm(getTerm(sign));
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            BigInteger tmp = new BigInteger(lexer.peek() + "1");
            term.addFactor(new Const(tmp));
            lexer.next();
        }
        term.addFactor(parseFactor());
        while (lexer.peek().equals("*")) {
            term.addFactor(getFactor());
        }
        return term;
    }

    /**
     * 1. expr
     * 2. const
     * 3. power
     * 4. sin
     * 5. cos
     * 6. diy_function
     * 7. sum
     */
    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            return getExpr();
        } else if (CONST.matcher(lexer.peek()).matches() ||
                SIGN.matcher(lexer.peek()).matches()) {
            return getConst();
        } else if (POW.matcher(lexer.peek()).matches()) {
            return getPow();
        } else if (SIN.matcher(lexer.peek()).matches()) {
            return getSin();
        } else if (COS.matcher(lexer.peek()).matches()) {
            return getCos();
        } else if (DIYFUNCTION.matcher(lexer.peek()).matches()) {
            return getDiyFunction();
        } else if (SUM.matcher(lexer.peek()).matches()) {
            return getSum();
        }
        return null;
    }
}
