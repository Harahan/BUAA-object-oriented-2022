import expr.Expr;
import expr.Factor;
import expr.Term;
import expr.Trigo;
import expr.Variable;

import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        Term term = parseTerm();
        expr.addTerm(term);
        while ("+-".contains(lexer.peek())) {
            term = parseTerm();
            expr.addTerm(term);
        }
        expr.sortTerm();
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term(0,BigInteger.ZERO);
        boolean symbol = true;
        if ("+-".contains(lexer.peek())) {
            if (lexer.peek().equals("-")) {
                symbol = false;
            }
            lexer.next();
        }
        term.setSymbol(symbol);
        Factor factor = parseFactor();
        term.addFactor(factor);
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Expr expr = parseExpr();
            lexer.next();
            if (lexer.peek().equals("*")) {
                lexer.next(); if (lexer.peek().equals("*")) {
                    lexer.next();
                    while (!Character.isDigit(lexer.peek().charAt(0))) {
                        lexer.next(); }
                    int index = Integer.parseInt(lexer.peek()); if (index == 0) {
                        expr = new Expr();
                        expr.addTerm(new Term(0,BigInteger.ONE)); }
                    Expr expr1 = new Expr(expr);
                    for (int i = 1; i < index; i++) {
                        expr.mulExpr(expr1); }
                    lexer.next(); } else {
                    lexer.last(); } }
            return expr;
        } else if (lexer.peek().equals("s") || lexer.peek().equals("c")) {
            int mode; if (lexer.peek().equals("s")) {
                mode = 1; } else {
                mode = 2; }
            lexer.next();
            Variable variable = (Variable) parseFactor();
            lexer.next();
            BigInteger index = BigInteger.ONE;
            if (lexer.peek().equals("*")) {
                lexer.next();
                if (lexer.peek().equals("*")) {
                    lexer.next();
                    while (!Character.isDigit(lexer.peek().charAt(0))) {
                        lexer.next(); }
                    index = new BigInteger(lexer.peek());
                    lexer.next(); } else {
                    lexer.last(); } }
            return new Trigo(mode,variable,index);
        } else {
            BigInteger coefficient = BigInteger.ONE;
            if ("+-".contains(lexer.peek())) {
                if (lexer.peek().equals("-")) {
                    coefficient = new BigInteger("-1"); }
                lexer.next(); }
            if (Character.isDigit(lexer.peek().charAt(0))) {
                coefficient = coefficient.multiply(new BigInteger(lexer.peek()));
                lexer.next();
                return new Variable(coefficient, 0); }
            else {
                lexer.next();
                int index = 1;
                if (lexer.peek().equals("*")) {
                    lexer.next();
                    if (lexer.peek().equals("*")) {
                        lexer.next();
                        while (!Character.isDigit(lexer.peek().charAt(0))) {
                            lexer.next(); }
                        index = Integer.parseInt(lexer.peek());
                        lexer.next(); } else {
                        lexer.last(); } }
                return new Variable(BigInteger.ONE,index); } } } }
