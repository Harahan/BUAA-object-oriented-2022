import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;
import expr.Expr;
import expr.DiyFunction;

public class MainClass {

    public static String parse(String str) {
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        expr = expr.expand().mergeSquare();
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
        System.out.println(parse(input));
    }
}
