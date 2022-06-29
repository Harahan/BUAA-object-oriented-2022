import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;
import expr.DiyFunction;
import expr.Expr;

public class MainClass {

    public static String parse(String str) {
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        expr = expr.expand();
        return expr.toString().
                replaceAll("^\\(", "").replaceAll("\\)$", "");
    }

    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int n = scanner.getCount();
        DiyFunction diyFunction = new DiyFunction();
        for (int i = 1; i <= n; i++) {
            String p = scanner.readLine();
            diyFunction.addDiyFunction(p);
        }
        String input = scanner.readLine();
        String str = parse(input).replaceAll("sin\\(0\\)", "(0)").replaceAll("cos\\(0\\)", "(1)");
        System.out.println(parse(str));
    }
}
