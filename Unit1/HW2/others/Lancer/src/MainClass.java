import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;
import expr.Expr;
import expr.Sum;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainClass {
    public static void main(String[] args) {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        // 获取自定义函数个数
        int cnt = scanner.getCount();
        // 读入自定义函数
        ArrayList<Function> functions = new ArrayList<>();
        for (int j = 0; j < cnt; j++) {
            String func = scanner.readLine();
            func = predone(func);
            Function function = new Function(func);
            functions.add(function);
        }
        String input = scanner.readLine();
        //预处理
        input = predone(input);
        while (input.contains("sum")) {
            int begin = input.indexOf("sum(");
            int pos = begin + 4;
            int kuohao = 1;
            StringBuilder sb = new StringBuilder();
            sb.append("sum(");
            while (kuohao != 0) {
                sb.append(input.charAt(pos));
                if (Character.toString(input.charAt(pos)).equals("(")) {
                    kuohao++;
                } else if (Character.toString(input.charAt(pos)).equals(")")) {
                    kuohao--;
                }
                pos++;
            }
            //System.out.println(sb.toString());
            Sum sum = new Sum(sb.toString());
            //System.out.println(sum.getSimplify());
            input = input.replace(sb.toString(), sum.getSimplify());
        }

        while (input.contains("f") || input.contains("g") || input.contains("h")) {
            int begin = input.contains("f") ? input.indexOf("f") : input.contains("g") ?
                    input.indexOf("g") : input.indexOf("h");
            int pos = begin + 2;
            int kuohao = 1;
            StringBuilder sb = new StringBuilder();
            sb.append(input.charAt(begin)).append("(");
            while (kuohao != 0) {
                sb.append(input.charAt(pos));
                if (Character.toString(input.charAt(pos)).equals("(")) {
                    kuohao++;
                } else if (Character.toString(input.charAt(pos)).equals(")")) {
                    kuohao--;
                }
                pos++;
            }
            //System.out.println(sb.toString());
            for (Function i : functions) {
                if (Objects.equals(i.getHead(), String.valueOf(input.charAt(begin)))) {
                    input = input.replace(sb.toString(), i.getSimplify(sb.toString()));
                }
            }
            //System.out.println(sum.getSimplify());
            //System.out.println(input);
        }
        input = predone(input);
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        HashMap<HashMap<String, Integer>, BigInteger> var1 = expr.calculate();
        PrintAns printAns = new PrintAns(var1);
        System.out.println(printAns.printans());
        //System.out.println(var1);
    }

    private static String predone(String input) {
        String input1 = input.replaceAll("\\s*", "");
        for (int i = 1; i <= 2; i++) {
            input1 = input1.replaceAll("\\x2B\\x2B", "+");
            input1 = input1.replaceAll("--", "+");
            input1 = input1.replaceAll("\\x2B-", "-");
            input1 = input1.replaceAll("-\\x2B", "-");
        }
        input1 = input1.replaceAll("\\x2A\\x2A\\x2B", "^");
        input1 = input1.replaceAll("\\x2A\\x2A", "^");
        return input1;
    }
    /*        StringBuilder anss = new StringBuilder();
        //第一项输出正数
        for (Integer i : ans.keySet()) {
            if (ans.get(i).compareTo(BigInteger.valueOf(0)) > 0) {
                anss.append(ans.get(i));
                if (i == 1) {
                    anss.append("*x");
                } else if (i == 2) {
                    anss.append("*x*x");
                } else if (i >= 3) {
                    anss.append("*x**").append(i);
                }
                ans.remove(i);
                break;
            }
        }
        //
        for (Integer i : ans.keySet()) {
            if (!ans.get(i).equals(BigInteger.valueOf(0))) {
                if (i == 0) {
                    anss.append("+").append(ans.get(i));
                } else if (i == 1) {
                    if (ans.get(i).equals(BigInteger.valueOf(1))) {
                        anss.append("+x");
                    } else if (ans.get(i).equals(BigInteger.valueOf(-1))) {
                        anss.append("-x");
                    } else {
                        anss.append("+").append(ans.get(i)).append("*x");
                    }
                } else if (i == 2) {
                    if (ans.get(i).equals(BigInteger.valueOf(1))) {
                        anss.append("+x*x");
                    } else if (ans.get(i).equals(BigInteger.valueOf(-1))) {
                        anss.append("-x*x");
                    } else {
                        anss.append("+").append(ans.get(i)).append("*x*x");
                    }
                } else {
                    if (ans.get(i).equals(BigInteger.valueOf(1))) {
                        anss.append("+x**").append(i);
                    } else if (ans.get(i).equals(BigInteger.valueOf(-1))) {
                        anss.append("-x**").append(i);
                    } else {
                        anss.append("+").append(ans.get(i)).append("*x**").append(i);
                    }
                }
            }
        }
        if (anss.length() == 0) {
            System.out.print(0);
        } else {
            if (anss.charAt(0) == '+') {
                anss.delete(0, 1);
            }
            String ansss = String.valueOf(anss);
            ansss = ansss.replaceAll("\\x2B-", "-");
            System.out.print(ansss);
        }*/
}
