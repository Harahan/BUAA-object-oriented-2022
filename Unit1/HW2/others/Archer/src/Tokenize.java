import parsing.Parser;
import parsing.RefT;
import parsing.Result;
import parsing.TList;
import parsing.TSel;
import parsing.TString;
import parsing.Token;

public class Tokenize {
    // Grammar Definition
    private static final RefT expr = new RefT();
    private static final RefT item = new RefT();
    private static final RefT factor = new RefT();
    private static final RefT factor_var = new RefT();
    private static final RefT factor_const = new RefT();
    private static final RefT factor_expr = new RefT();
    private static final RefT func_pow = new RefT();
    private static final RefT func_tri = new RefT();
    private static final RefT power = new RefT();
    private static final RefT signed_decimal = new RefT();
    private static final RefT decimal = new RefT();
    private static final RefT empty = new RefT();
    private static final RefT sign = new RefT();
    private static final RefT func_def = new RefT();
    private static final RefT var_func = new RefT();
    private static final RefT func_call = new RefT();
    private static final RefT func_name = new RefT();
    private static final RefT func_sum = new RefT();
    private static final RefT func_expr = new RefT();
    private static final RefT sum_expr = new RefT();

    // String Constant Tokens
    private static final Token s_asterisk = new TString("*");
    private static final Token s_sum = new TString("sum");
    private static final Token s_comma = new TString(",");
    private static final Token s_equal = new TString("=");
    private static final Token s_plus = new TString("+");
    private static final Token s_power = new TString("**");
    private static final Token s_parLeft = new TString("(");
    private static final Token s_parRight = new TString(")");
    private static final Token s_charI = new TString("i");
    private static final Token s_sin = new TString("sin");
    private static final Token s_cos = new TString("cos");

    private static int initValue = staticInit();

    private static int staticInit() {
        // definition
        // expr = empty [sign empty] item {empty sign empty item} empty
        expr.setTokenList(empty, new TList(sign, empty).opt(), item,
                new TList(empty, sign, empty, item).rep(), empty);
        // item = [sign empty] factor { empty "*" empty factor }
        item.setTokenList(new TList(sign, empty).opt(), factor,
                new TList(empty, s_asterisk, empty, factor).rep());
        // factor = factor_var | factor_const | factor_expr
        factor.setTokenSel(factor_var, factor_const, factor_expr);
        // factor_var = func_pow | func_tri | func_call | func_sum
        factor_var.setTokenSel(func_pow, func_tri, func_call, func_sum);
        // factor_const = signed_decimal
        factor_const.setTokenList(signed_decimal);
        // factor_expr = "(" expr ")" [empty power]
        factor_expr.setTokenList(s_parLeft, expr, s_parRight, new TList(empty, power).opt());
        // func_pow = (var_func | "i") [empty power]
        func_pow.setTokenList(new TSel(var_func, s_charI), new TList(empty, power).opt());
        // func_tri = ("sin" | "cos") empty "(" empty factor empty ")" [empty power]
        func_tri.setTokenList(new TSel(s_sin, s_cos), empty, s_parLeft, empty, factor, empty,
                s_parRight, new TList(empty, power).opt());
        // power = "**" empty ["+"] decimal
        power.setTokenList(s_power, empty, s_plus.opt(), decimal);
        // signed_decimal = [sign] decimal
        signed_decimal.setTokenList(sign.opt(), decimal);
        // decimal = regex: [0-9]+
        decimal.setToken(TString.fromPatternString("[0-9]+"));
        // empty = regex: [ \t]*
        empty.setToken(TString.fromPatternString("[ \t]*"));
        // sign = regex: [+-]
        sign.setToken(TString.fromPatternString("[+-]"));
        /*
         * func_def = func_name empty "(" empty var_func empty {"," empty var_func, empty}[0-2] ")"
         * ... empty "=" empty func_expr
         */
        func_def.setTokenList(func_name, empty, s_parLeft, empty, var_func, empty,
                new TList(s_comma, empty, var_func, empty).rep(0, 2), s_parRight, empty, s_equal,
                empty, func_expr);
        // var_func = regex: [xyz]
        var_func.setToken(TString.fromPatternString("[xyz]"));
        // func_call = func_name empty "(" empty factor empty {"," empty factor, empty}[0-2]
        // ")"
        func_call.setTokenList(func_name, empty, s_parLeft, empty, factor, empty,
                new TList(s_comma, empty, factor, empty).rep(0, 2), s_parRight);
        // func_name = regex: [fgh]
        func_name.setToken(TString.fromPatternString("[fgh]"));
        /*
         * func_sum = "sum" "(" empty "i" empty { "," empty factor_const empty }[2] "," empty ...
         * ... sum_expr empty ")"
         */
        func_sum.setTokenList(s_sum, s_parLeft, empty, s_charI, empty,
                new TList(s_comma, empty, factor_const, empty).rep(2, 2), s_comma, empty, sum_expr,
                empty, s_parRight);
        // func_expr = expr
        func_expr.setTokenList(expr);
        // sum_expr = factor
        sum_expr.setTokenList(factor);

        initValue = 0;
        return initValue;
    }

    public static Result parseExpr(Parser p) {
        return expr.parse(p);
    }

    public static Result parseFuncDef(Parser p) {
        return func_def.parse(p);
    }
}
