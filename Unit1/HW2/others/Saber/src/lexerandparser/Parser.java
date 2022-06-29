package lexerandparser;

import factory.FuncFactory;
import formula.Expression;
import formula.Factor;
import formula.Term;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;
    private final FuncFactory funcFactory;

    public Parser(Lexer lexer, FuncFactory funcFactory) {
        this.lexer = lexer;
        this.funcFactory = funcFactory;
    }

    public Expression expressionParser() {
        Expression expression = new Expression();
        boolean signNegative;
        boolean sign = false;
        if (lexer.peek().matches("[+-]")) {
            sign = lexer.peek().equals("-");
            lexer.get();
        }
        Term termFirst = termParser();
        if (sign) {
            expression.sub(termFirst);
        } else {
            expression.add(termFirst);
        }
        while (lexer.peek().matches("[-+]")) {
            if (lexer.peek().equals("+")) {
                lexer.get();
                Term termSecond = termParser();
                expression.add(termSecond);
            } else if (lexer.peek().equals("-")) {
                lexer.get();
                Term termSecond = termParser();
                expression.sub(termSecond);
            }
        }
        return expression;
    }

    public Term termParser() {
        Term term = new Term(factorParser());
        while (lexer.peek().equals("*")) {
            lexer.get();
            Factor factorSecond = factorParser();
            term = term.mul(factorSecond);
        }
        return term;
    }

    public Factor factorParser() {
        Factor factor = new Factor();
        if (lexer.peek().equals("(")) {
            lexer.get();
            factor = expressionParser();
            lexer.get();
            if (lexer.peek().equals("^")) {
                lexer.get();
                BigInteger index = numParser();
                factor = factor.power(index);
            }
        } else if (lexer.peek().equals("x")) {
            lexer.get();
            if (lexer.peek().equals("^")) {
                lexer.get();
                BigInteger index = numParser();
                factor.append(index,
                        BigInteger.ONE,
                        BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, true);
            } else {
                factor.append(BigInteger.ONE,
                        BigInteger.ONE,
                        BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, true);
            }
        } else if (lexer.peek().matches("[+-]") ||
                Character.isDigit(lexer.peek().charAt(0))) {
            BigInteger coefficient = numParser();
            factor.append(BigInteger.ZERO,
                    coefficient,
                    BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, true);
        } else {
            if (lexer.peek().equals("SIN") || lexer.peek().equals("cos")) {
                String tri = lexer.peek();
                lexer.get();
                factor = triParser(tri);
                if (lexer.peek().equals("^")) {
                    lexer.get();
                    BigInteger index = numParser();
                    factor = factor.power(index);
                }
            } else if (lexer.peek().equals("sum")) {
                lexer.get();
                factor = (new SumParser(lexer, funcFactory)).sumParser();
            } else {
                funcFactory.setLexer(lexer);
                factor = funcFactory.funcParser(lexer.peek());
            }
        }
        return factor;
    }

    public BigInteger numParser() {
        int result;
        boolean isNegate = false;
        if (lexer.peek().matches("[+-]")) {
            if (lexer.peek().equals("-")) {
                isNegate = true;
            }
            lexer.get();
        }
        String s = lexer.peek();
        lexer.get();
        BigInteger i = new BigInteger(s);
        return isNegate ? i.negate() :
                i;
    }

    public Factor triParser(String tri) {
        Factor factor;
        BigInteger inIndex = BigInteger.ZERO;
        BigInteger constant = BigInteger.ONE;
        lexer.get();//pass"("
        Term term = termParser();
        if (tri.equals("SIN")) {
            factor = new Factor(term, true);
        } else {
            factor = new Factor(term, false);
        }
        lexer.get(); //pass ")"
        return factor;
    }
}
