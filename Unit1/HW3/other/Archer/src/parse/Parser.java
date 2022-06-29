package parse;

import expr.Expr;
import expr.Term;
import expr.Factor;
import expr.Number;
import expr.PowerFunc;
import expr.TriFunc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private final Lexer lexer;

    private HashMap<String, CustomFunc> funcHashMap;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void setFuncHashMap(HashMap<String, CustomFunc> funcHashMap) {
        this.funcHashMap = funcHashMap;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();

        boolean exprSign = false;

        if (lexer.peek().isEmpty()) {
            lexer.next();
        }

        if (lexer.peek().equals("-")) {
            exprSign = true;
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }

        Term term = parseTerm(exprSign);
        expr.addTerm(term);

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            boolean exprSign1 = lexer.peek().equals("-");
            lexer.next();

            Term term1 = parseTerm(exprSign1);
            expr.addTerm(term1);
        }

        return expr;
    }

    public Term parseTerm(boolean exprSign) {
        Term term = new Term();
        boolean termSign;

        if (lexer.peek().equals("-")) {
            termSign = !exprSign;
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            termSign = exprSign;
            lexer.next();
        } else {
            termSign = exprSign;
        }

        term.setNegative(termSign);
        Factor fact = parseFactor();
        term.addFactor(fact);

        while (lexer.peek().equals("*")) {
            lexer.next();
            Factor fact1 = parseFactor();
            term.addFactor(fact1);
        }

        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            /*
            if (((Expr)expr).isSingle()) {
                expr = ((Expr) expr).getTerms().get(0).getFactors().get(0);
            }

             */
            if (lexer.peek().equals("**")) {
                lexer.next();
                if (lexer.peek().equals("+")) {
                    lexer.next();
                }
                int power = Integer.parseInt(lexer.peek());
                lexer.next();

                if (power == 0) {
                    return new Number(BigInteger.ONE);
                }
                Expr newExpr = new Expr();
                Term term = new Term();
                for (int i = 0; i < power; i++) {
                    term.addFactor(expr);
                }
                newExpr.addTerm(term);

                return newExpr;
            } else {
                return expr;
            }

        } else if ("i xyz".contains(lexer.peek())) {
            return parsePowerFunc();

        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            return parseTriFunc();

        } else if ("fgh".contains(lexer.peek())) {
            return parseCustomFunc();

        } else if (lexer.peek().equals("sum")) {
            return parseSumFunc();

        } else {
            return parseNumber();
        }
    }

    public Number parseNumber() {
        boolean sign = false;
        if (lexer.peek().equals("-")) {
            sign = true;
            lexer.next();
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }
        BigInteger num = new BigInteger(lexer.peek());
        if (sign) {
            num = num.negate();
        }
        lexer.next();
        return new Number(num);
    }

    public Factor parsePowerFunc() {
        String varName = lexer.peek();
        lexer.next();
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            int power = Integer.parseInt(lexer.peek());
            lexer.next();

            if (power == 0) {
                return new Number(BigInteger.ONE);
            }
            return new PowerFunc(varName, power);
        } else {
            return new PowerFunc(varName, 1);
        }
    }

    public Factor parseTriFunc() {
        String type = lexer.peek();
        lexer.next();
        lexer.next();
        Factor core = parseFactor();
        lexer.next();
        if (lexer.peek().equals("**")) {
            lexer.next();
            if (lexer.peek().equals("+")) {
                lexer.next();
            }
            int power = Integer.parseInt(lexer.peek());
            lexer.next();

            if (power == 0) {
                return new Number(BigInteger.ONE);
            }

            if (core instanceof Number) {
                if (((Number) core).getNum().equals(BigInteger.ZERO)) {
                    if (type.equals("sin")) {
                        return new Number(BigInteger.ZERO);
                    } else {
                        return new Number(BigInteger.ONE);
                    }
                }
            }
            return new TriFunc(type, core, power);
        } else {
            if (core instanceof Number) {
                if (((Number) core).getNum().equals(BigInteger.ZERO)) {
                    if (type.equals("sin")) {
                        return new Number(BigInteger.ZERO);
                    } else {
                        return new Number(BigInteger.ONE);
                    }
                }
            }
            return new TriFunc(type, core, 1);
        }
    }

    public Factor parseSumFunc() {
        lexer.next();
        lexer.next();
        String loopVar = lexer.peek();
        lexer.next();
        lexer.next();
        final Number start = parseNumber();
        lexer.next();
        final Number end = parseNumber();
        lexer.next();
        Factor factor = parseFactor();
        String sb = factor.toString();
        lexer.next();

        if (start.getInt() > end.getInt()) {
            return new Number(BigInteger.ZERO);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(sb.replace("sin", "SIN")
                    .replace(loopVar, "(" + start.getInt() + ")")
                    .replace("SIN", "sin"));
            for (int i = start.getInt() + 1; i <= end.getInt(); i++) {
                sb2.append("+");
                sb2.append(sb.replace("sin", "SIN")
                        .replace(loopVar, "(" + i + ")")
                        .replace("SIN", "sin"));
            }
            String fact = "(" + sb2 + ")";
            Lexer lexer1 = new Lexer(fact);
            Parser parser1 = new Parser(lexer1);
            return parser1.parseExpr();
        }
    }

    public Factor parseCustomFunc() {
        final String funcName = lexer.peek();
        lexer.next();
        lexer.next();

        ArrayList<String> vars = new ArrayList<>();

        while (! lexer.peek().equals(")")) {
            Factor fact = parseFactor();
            vars.add(fact.toString());

            if (lexer.peek().equals(",")) {
                lexer.next();
            }
        }
        lexer.next();

        CustomFunc customFunc = funcHashMap.get(funcName);

        String func = customFunc.getFunction();
        ArrayList<String> list = customFunc.getVariables();

        for (int i = 0; i < vars.size(); i++) {
            /*
            if (list.get(i).equals("u") || list.get(i).equals("y") || list.get(i).equals("z")) {
                func = func.replaceAll(list.get(i), vars.get(i));
            } else {
                func = func.replaceAll(list.get(i), "(" + vars.get(i) + ")");
            }

             */

            func = func.replaceAll(list.get(i), "(" + vars.get(i) + ")");
            // func = func.replaceAll(list.get(i), vars.get(i));
        }

        Lexer lexer1 = new Lexer(func);
        Parser parser1 = new Parser(lexer1);
        return parser1.parseExpr();
    }

}
