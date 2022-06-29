import expr.Expr;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Scanner;
/**
 * exper -> exper + term | term
 * term -> term * factor | factor
 * factor -> num_factor | exper_factor
 * exper_factor -> '(' exper ')'  
 */

public class MainClass {
    public static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        Lexer lexer = new Lexer(input); //处理一下表达式开始是数字还是符号
        Parser parser = new Parser(lexer); //解析元素

        Expr expr = parser.parseExpr(); 
        System.out.println(expr);
    }
}
