import expr.Expr;

// 需要先将官方包中用到的工具类import进来
import com.oocourse.spec2.ExprInput;
import com.oocourse.spec2.ExprInputMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String expr;

    public static void main(String[] argv) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        // 获取自定义函数个数
        int cnt = scanner.getCount();
        // 读入自定义函数
        String[] func = {"","",""};
        for (int i = 0; i < cnt; i++) {
            func[i] = scanner.readLine();
            func[i] = func[i].replaceAll("x","m");
        }
        // 一般读入模式下，读入一行字符串时使用readLine()方法，在这里我们使用其读入表达式
        expr = scanner.readLine();
        // 表达式括号展开相关的逻辑
        process(func);
        Lexer lexer = new Lexer(process(func));
        Parser parser = new Parser(lexer);
        Expr ex = parser.parseExpr();
        ex.merge();
        System.out.println(ex);
    }

    private static String process(String[] func) {
        expr = expr.replaceAll(" ","");
        expr = expr.replaceAll("\t","");
        for (String item:func) {
            if (item.equals("")) {
                continue; }
            item = item.replaceAll(" ","");
            item = item.replaceAll("\t","");
            String[] function = item.split("=");
            Pattern pattern = Pattern.compile(item.charAt(0) + "\\((([^()]+|(\\([^()]+\\)))+)\\)");
            Matcher matcher = pattern.matcher(item);
            if (matcher.find()) {
                String[] contents = matcher.group(1).split(",");
                Matcher matcher1 = pattern.matcher(expr);
                while (matcher1.find()) {
                    String[] targets = matcher1.group(1).split(",");
                    String replacement = "(" + function[1] + ")";
                    for (int i = 0; i < contents.length; i++) {
                        replacement = replacement.replaceAll(contents[i],"(" + targets[i] + ")"); }
                    expr = expr.replace(matcher1.group(0),replacement);
                    matcher1 = pattern.matcher(expr);
                }
            }
        }
        expr = expr.replaceAll("sin","snn");
        while (expr.contains("sum")) {
            String[] contents = processSum().split(",");
            String replacement;
            if (Integer.parseInt(contents[1]) > Integer.parseInt(contents[2])) {
                replacement = "0";
            } else {
                replacement = contents[3].replaceAll("i",contents[1]);
            }
            int i = Integer.parseInt(contents[1]) + 1;
            for (; i <= Integer.parseInt(contents[2]); i++) {
                replacement = replacement + "+" + contents[3].replaceAll("i",String.valueOf(i));
            }
            replacement = "(" + replacement + ")";
            expr = expr.replace("sum(" + processSum() + ")",replacement);
        }
        Pattern pattern = Pattern.compile("[+-]{2}");
        Matcher matcher = pattern.matcher(expr);
        while (matcher.find()) {
            expr = expr.replaceAll("\\+\\+","\\+");
            expr = expr.replaceAll("-\\+","-");
            expr = expr.replaceAll("\\+-","-");
            expr = expr.replaceAll("--","\\+");
            matcher = pattern.matcher(expr);
        }
        pattern = Pattern.compile("\\((\\([^()]+\\))\\)");
        matcher = pattern.matcher(expr);
        while (matcher.find()) {
            expr = expr.replace(matcher.group(0),matcher.group(1));
            matcher = pattern.matcher(expr);
        }
        return expr;
    }

    public static String processSum() {
        int index = expr.indexOf("sum") + 4;
        StringBuilder sb = new StringBuilder();
        int num = 1;
        while (true) {
            if (expr.charAt(index) == '(') {
                num++;
            } else if (expr.charAt(index) == ')') {
                num--;
            }
            if (num == 0) {
                break;
            }
            sb.append(expr.charAt(index));
            index++;
        }
        return sb.toString();
    }
}
