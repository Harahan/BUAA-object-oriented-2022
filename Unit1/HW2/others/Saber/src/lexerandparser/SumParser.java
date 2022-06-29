package lexerandparser;

import factory.FuncFactory;
import formula.Expression;
import formula.Factor;

import java.math.BigInteger;

public class SumParser {
    private Lexer lexer;
    private FuncFactory funcFactory;

    public SumParser(Lexer lexer, FuncFactory funcFactory) {
        this.lexer = lexer;
        this.funcFactory = funcFactory;
    }

    public Factor sumParser() {
        lexer.get();//pass "("
        lexer.get();//pass "i"
        lexer.get();//pass ","
        boolean isNegate = false;
        if (lexer.peek().matches("[+-]")) {
            if (lexer.peek().equals("-")) {
                isNegate = true;
            }
            lexer.get();
        }
        BigInteger s = isNegate ? new BigInteger(lexer.peek()).negate()
                : new BigInteger(lexer.peek());
        lexer.get();
        lexer.get();
        isNegate = false;
        if (lexer.peek().matches("[+-]")) {
            if (lexer.peek().equals("-")) {
                isNegate = true;
            }
            lexer.get();
        }

        BigInteger t = isNegate ? new BigInteger(lexer.peek()).negate()
                : new BigInteger(lexer.peek());
        lexer.get();
        lexer.get();
        StringBuilder sb = new StringBuilder();
        int cnt = 1;
        while (true) {
            if (lexer.peek().equals("(")) {
                cnt++;
            }
            if (lexer.peek().equals(")")) {
                cnt--;
            }
            if (cnt == 0) {
                break;
            }
            sb.append(lexer.peek());
            lexer.get();
        }
        lexer.get();
        String formula = sb.toString();
        Expression expression = new Expression();
        for (BigInteger i = s; !(i.compareTo(t) > 0); i = i.add(BigInteger.ONE)) {
            String formulaNow = formula.replaceAll("i", i.toString());
            Lexer lexernew = new Lexer(formulaNow);
            Parser parser = new Parser(lexernew, funcFactory);
            expression.add(parser.expressionParser());
        }
        return expression;
    }
}
