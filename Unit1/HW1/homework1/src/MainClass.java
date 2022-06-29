import expr.Expr;

import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {

    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine();
        Expr expr = null;
        try {
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(lexer);
            expr = parser.parseExpr();
            if (!lexer.peek().equals("END")) {
                System.out.println("Wrong Format!");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Wrong Format!");
            System.exit(0);
        }
        expr = expr.expand();
        expr.simplify();
        System.out.println(expr);
    }
}