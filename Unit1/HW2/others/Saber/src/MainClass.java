import formula.Expression;
import factory.FuncFactory;
import lexerandparser.Lexer;
import lexerandparser.Parser;

// 需要先将官方包中用到的工具类import进来
import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class MainClass {
    public static String stringDispose(String s) {
        String input = s;
        input = input.replaceAll("\\s+", "");
        input = input.replace("+-", "-");
        input = input.replace("-+", "-");
        input = input.replace("++", "+");
        input = input.replace("--", "+");
        input = input.replace("**", "^");
        input = input.replace("+-", "-");
        input = input.replace("-+", "-");
        input = input.replace("++", "+");
        input = input.replace("--", "+");
        input = input.replace("sin", "SIN");
        return input;
    }

    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        int functionNum = scanner.getCount();
        FuncFactory funcFactory = new FuncFactory(functionNum, scanner);//建立工厂
        String input = scanner.readLine();
        input = stringDispose(input);
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer, funcFactory);

        Expression expression = parser.expressionParser();
        System.out.println(expression.toString());
    }
}
