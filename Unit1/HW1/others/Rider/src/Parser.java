import java.math.BigInteger;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr exprNew = new Expr();
        exprNew = parseTerm();
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            Expr exprNext = new Expr();
            if (lexer.peek().equals("+")) {
                lexer.next();
                exprNext = parseTerm();
                exprNew.calculateA(exprNext.getExpr());
            }
            else {
                lexer.next();
                exprNext = parseTerm();
                exprNew.calculateS(exprNext.getExpr());
            }
        }
        return exprNew;
    }

    public Expr parseTerm() {
        Expr term = new Expr();
        term = parseFactor();
        while (lexer.peek().equals("*")) {
            lexer.next();
            Expr termNext = new Expr();
            termNext = parseFactor();
            term.calculateM(termNext.getExpr());
        }
        return term;
    }

    public Expr parseFactor() {
        Expr exprNew = new Expr();
        if (lexer.peek().equals("+") && lexer.is()) {
            lexer.next();
            exprNew = parseFactor();
        } else if (lexer.peek().equals("-") && lexer.is()) {
            lexer.next();
            exprNew = parseFactor();
            exprNew.negateExpr();
        } else {
            if (lexer.peek().equals("(")) {
                lexer.next();
                exprNew = parseExpr();
                lexer.next();
                if (lexer.peek().equals("**")) {
                    lexer.next();
                    if (lexer.peek().equals("+")) {
                        lexer.next();
                    }
                    int exp = Integer.valueOf(lexer.peek());
                    if (exp == 0) {
                        Expr exprNew1 = new Expr();
                        exprNew1.addTerm(0,BigInteger.valueOf(1));
                        lexer.next();
                        return exprNew1;
                    }
                    else {
                        HashMap<Integer,BigInteger> tmp = new HashMap<>();
                        tmp = (HashMap<Integer, BigInteger>) exprNew.getExpr().clone();
                        for (int i = 1;i < exp;i++)
                        {
                            exprNew.calculateM(tmp);
                        }
                    }
                    lexer.next();
                }
            }
            else {
                String op = "(\\-?\\+?\\d*)?(\\*)?(x\\*{2})?(x)?(\\+?\\d+)?";
                Pattern pattern = Pattern.compile(op);
                Matcher matcher = pattern.matcher(lexer.peek() + lexer.remaining());
                if (matcher.find()) {
                    int b = 0;
                    BigInteger a = new BigInteger("0");
                    if (matcher.group(1) == null || matcher.group(1).length() == 0) {
                        a = new BigInteger("1");
                        if (matcher.group(5) == null || matcher.group(5).length() == 0) {
                            b = 1;
                            lexer.next();
                        }
                        else {
                            if (matcher.group(3) == null || matcher.group(3).length() == 0) {
                                b = 1;
                                lexer.next();
                            }
                            else {
                                lexer.next();
                                lexer.next();
                                b = Integer.valueOf(matcher.group(5));
                                if (matcher.group(5).indexOf('+') != -1) {
                                    lexer.next();
                                }
                                lexer.next();
                            }
                        }
                    } else {
                        if (matcher.group(1).equals("+")) {
                            a = new BigInteger("1");
                        } else if (matcher.group(1).equals("-")) {
                            a = new BigInteger("-1");
                        } else {
                            if (matcher.group(1).contains("+")  || matcher.group(1).contains("-")) {
                                lexer.next();
                            }
                            a = new BigInteger(matcher.group(1));
                            lexer.next();
                        }
                        if (matcher.group(5) == null || matcher.group(5).length() == 0) {
                            if (matcher.group(4) == null || matcher.group(4).length() == 0) {
                                b = 0;
                            } else {
                                b = 1;
                                lexer.next();
                                lexer.next();
                            }
                        } else {
                            if (matcher.group(3) == null || matcher.group(3).length() == 0) {
                                if (matcher.group(4) == null || matcher.group(4).length() == 0) {
                                    b = 0;
                                }
                                else {
                                    b = 1;
                                    lexer.next();
                                    lexer.next();
                                }
                            }
                            else {
                                b = Integer.valueOf(matcher.group(5));
                                lexer.next();
                                lexer.next();
                                lexer.next();
                                if (matcher.group(5).indexOf('+') != -1) {
                                    lexer.next();
                                }
                                lexer.next();
                            }
                        }
                    }
                    exprNew.addTerm(b, a);
                }
            }
        }
        return exprNew;
    }
}