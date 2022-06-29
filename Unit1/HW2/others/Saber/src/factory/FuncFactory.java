package factory;

import formula.Factor;
import formula.Func;
import lexerandparser.Lexer;
import lexerandparser.Parser;

import java.util.ArrayList;

import com.oocourse.spec2.ExprInput;

public class FuncFactory {
    private ArrayList<Func> funcs = new ArrayList<>();
    private Lexer lexer;

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public FuncFactory(int num, ExprInput scanner) {
        for (int i = 0; i < num; i++) {
            String functionDef = scanner.readLine();
            functionDef = functionDef.replaceAll("\\s+", "");
            functionDef = functionDef.replace("+-", "-");
            functionDef = functionDef.replace("-+", "-");
            functionDef = functionDef.replace("++", "+");
            functionDef = functionDef.replace("--", "+");
            functionDef = functionDef.replace("**", "^");
            functionDef = functionDef.replace("+-", "-");
            functionDef = functionDef.replace("-+", "-");
            functionDef = functionDef.replace("++", "+");
            functionDef = functionDef.replace("--", "+");
            functionDef = functionDef.replace("sin", "SIN");

            String[] matcherOne = functionDef.split("=");
            String name = matcherOne[0].substring(0, 1);
            Func func = new Func(name, matcherOne[1]);//matcherOne[1]是公式
            matcherOne[0] = matcherOne[0].replaceAll("[fgh()]", "");
            String[] matcherTwo = matcherOne[0].split(",");
            for (int j = 0; j < matcherTwo.length; j++) {
                func.addParameter(matcherTwo[j]);//hidden bug is null pointer
            }
            funcs.add(func);
        }
    }

    public Factor funcParser(String funcName) {
        ArrayList<String> parameters;
        Parser parser;
        for (Func func : funcs) {
            if (func.getName().equals(funcName)) {
                int cnt = -1;
                lexer.get();
                lexer.get();//pass "("
                String formula = func.getFormula();
                parameters = func.getArrayList();
                int cntBracket = 1;
                while (cntBracket != 0) {
                    StringBuilder sb = new StringBuilder();
                    while (!lexer.peek().equals(",")) {
                        if (lexer.peek().equals("(")) {
                            cntBracket++;
                        }
                        if (lexer.peek().equals(")")) {
                            cntBracket--;
                        }
                        if (cntBracket == 0) {
                            break;
                        } else {
                            sb.append(lexer.peek());
                            lexer.get();
                        }
                    }
                    cnt++;
                    String replacement = sb.toString();
                    formula = formula.replaceAll(parameters.get(cnt),
                            "(" + replacement + ")");
                    lexer.get();
                }
                Lexer lexerNow = new Lexer(formula);
                parser = new Parser(lexerNow, this);
                return parser.expressionParser();
            }
        }
        return null;
    }
}
