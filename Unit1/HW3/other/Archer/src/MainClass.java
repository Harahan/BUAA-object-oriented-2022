import expr.Expr;
import parse.CustomFuncs;
import parse.Lexer;
import parse.Parser;
import poly.Polynomial;

// 需要先将官方包中用到的工具类import进来
import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;

// import parse.CustomFunc;
// import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        CustomFuncs customFuncs = new CustomFuncs();

        // 获取自定义函数个数
        int cnt = scanner.getCount();

        // 读入自定义函数
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            // 存储或者解析逻辑
            customFuncs.addFunc(func);
        }

        /*
        HashMap<String, CustomFunc> funcHashMap = customFuncs.getCustomFuncHashMap();
        for (parse.CustomFunc customFunc : funcHashMap.values()) {
            System.out.println(customFunc.getName());
            System.out.println(customFunc.getVariables());
            System.out.println(customFunc.getFunction());
        }

         */

        // 读入最后一行表达式
        String input = scanner.readLine();

        // 表达式括号展开相关的逻辑
        String formula = input.trim().replaceAll("\\s", "");

        Lexer lexer = new Lexer(formula);
        Parser parser = new Parser(lexer);
        parser.setFuncHashMap(customFuncs.getCustomFuncHashMap());

        Expr expr = parser.parseExpr();
        // System.out.println(expr);
        Polynomial polynomial = expr.toPoly();
        String result = polynomial.toString()
                .replaceAll("(cos\\(\\()([-])((\\d+\\*)?x)(\\*\\*\\d+)?(\\)\\))", "$1$3$5$6");

        Lexer lexer1 = new Lexer(result);
        Parser parser1 = new Parser(lexer1);
        parser1.setFuncHashMap(customFuncs.getCustomFuncHashMap());

        Expr expr1 = parser1.parseExpr();
        Polynomial polynomial1 = expr1.toPoly();
        String result1 = polynomial1.toString()
                .replaceAll("(cos\\(\\()([-])((\\d+\\*)?x)(\\*\\*\\d+)?(\\)\\))", "$1$3$5$6");

        while (result1.length() < result.length()) {
            result = result1;
            Lexer lexer2 = new Lexer(result1);
            Parser parser2 = new Parser(lexer2);
            parser2.setFuncHashMap(customFuncs.getCustomFuncHashMap());

            Expr expr2 = parser2.parseExpr();
            Polynomial polynomial2 = expr2.toPoly();
            result1 = polynomial2.toString()
                    .replaceAll("(cos\\(\\()([-])((\\d+\\*)?x)(\\*\\*\\d+)?(\\)\\))", "$1$3$5$6");
        }

        String finalResult = result1
                .replaceAll("([+-]?(\\d+\\*)?x)(\\*\\*2)([+-]|$)", "$1\\*x$4"); // (非三角)指数为2的情况

        System.out.println(finalResult);
    }

}
