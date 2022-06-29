import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine();
        input = input.replaceAll("\\s*", "");
        //System.out.println(input);
        char top = input.charAt(0);
        if ("+-".indexOf(top) != -1) {
            input = "0" + input;
        }
        StringBuilder newline = new StringBuilder();
        int i;

        for (i = 0;i < input.length() - 1;) {
            char one = input.charAt(i);
            char two = input.charAt(i + 1);
            if (one == '(' && "+-".indexOf(two) != -1) {
                newline.append(one).append("0");
                i++;
            }
            else if ("+-".indexOf(one) != -1) {
                int check = 1;
                if (one == '-') {
                    check *= -1;
                }
                while ("+-".indexOf(input.charAt(++i)) != -1) {
                    if (input.charAt(i) == '-') {
                        check *= -1;
                    }
                }
                if (check == 1) {
                    newline.append("+");
                }
                else {
                    newline.append("-");
                }
            }
            else {
                newline.append(one);
                i++;
            }
        }

        newline.append(input.charAt(i));
        //System.out.println(newline);
        Lexer lexer = new Lexer(newline.toString());
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        expr.setPower(1);
        //System.out.println(expr);
        expr.simplify(true);
    }
}
