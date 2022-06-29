import expr.Expr;
import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine();
        input = input.replaceAll("\\s","");
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        expr.print();
        System.out.println();
    }
}

/*
0
sin(x)**2+cos(x)**2
 */