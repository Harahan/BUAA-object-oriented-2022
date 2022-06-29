import parsing.Parser;
import parsing.Result;

import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

public class MainClass {
    public static void main(String[] args) {
        Convert convert = new Convert();
        ExprInput input = new ExprInput(ExprInputMode.NormalMode);

        int n = input.getCount();
        for (int i = 0; i < n; i++) {
            Parser p = new Parser(input.readLine().trim());
            Result r = Tokenize.parseFuncDef(p);
            // System.out.println(p.ended() + " " + r);
            // System.out.println(
            convert.doFuncDef(r);
            // );
        }

        Parser p = new Parser(input.readLine().trim());
        Result r = Tokenize.parseExpr(p);
        // System.out.println(p.ended() + " " + r);
        System.out.println(convert.doExpr(r).toString().replaceFirst("^\\+*", ""));
    }
}
