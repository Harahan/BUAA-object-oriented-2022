import java.math.BigInteger;
import java.util.HashMap;
import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

public class Mainclass {

    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String input = scanner.readLine();
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        HashMap<Integer, BigInteger> print;
        print = (HashMap<Integer, BigInteger>) expr.getExpr().clone();
        int flag = 0;
        BigInteger j = new BigInteger("0");
        BigInteger k = new BigInteger("1");
        BigInteger l = new BigInteger("-1");
        for (Integer i :print.keySet()) {
            if (print.get(i).equals(j)) {
                continue;
            }
            if (flag == 1 && print.get(i).signum() == 1) {
                System.out.print("+");
            }
            if (print.get(i).equals(k)) {
                if (i == 0) {
                    System.out.print("1");
                } else if (i == 1) {
                    System.out.print("x");
                } else if (i == 2) {
                    System.out.print("x*x");
                } else {
                    System.out.print("x**" + i);
                }
            } else if (print.get(i).equals(l)) {
                if (i == 0) {
                    System.out.print("-1");
                } else if (i == 1) {
                    System.out.print("-x");
                } else if (i == 2) {
                    System.out.print("-x*x");
                } else {
                    System.out.print("-x**" + i);
                }
            } else {
                if (i == 0) {
                    System.out.print(print.get(i));
                } else if (i == 1) {
                    System.out.print(print.get(i) + "*x");
                } else if (i == 2) {
                    System.out.print(print.get(i) + "*x*x");
                } else {
                    System.out.print(print.get(i) + "*x**" + i);
                }
            }
            flag = 1;
        }
        if (flag == 0) {
            System.out.println("0");
        }
    }
}
