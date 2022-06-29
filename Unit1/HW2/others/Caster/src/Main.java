import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class Main {
    
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        // 获取自定义函数个数
        int cnt = scanner.getCount();
        // 读入自定义函数
        SelfDefFuncs selfDefFuncs = new SelfDefFuncs();
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            // 存储或者解析逻辑
            selfDefFuncs.readFunc(func);
        }
        String input = scanner.readLine();
        input = input.replace("**", "^").replaceAll("\\s", "");
        input = selfDefFuncs.displaceSelfDefFuncs(input);
        SumFunc sumfuncs = new SumFunc();
        input = sumfuncs.displaceSumFuncs(input);
        //System.out.println(input);
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr(false);
        Polynomial polynomial = expr.toPolynomial();
        System.out.println(polynomial.toString());
    }
}
