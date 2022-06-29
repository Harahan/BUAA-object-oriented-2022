package mainpack;

import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import lex.Lexer;
import syntax.Parser;
import syntax.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class MainClass
{
    public static void main(String[] args)
    {
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);
        String expression = scanner.readLine();
        if (expression.matches("[\\x20\\x09]*"))
        {
            System.out.println("Blank expression");
            return;
        }
        Parser parser = new Parser(new Lexer(expression));
        HashMap<BigInteger, Polynomial> result = parser.calculate();
        if (result == null)
        {
            return;
        }
        if (result.isEmpty())
        {
            System.out.println("0");
            return;
        }
        print(result);
    }

    private static void print(HashMap<BigInteger, Polynomial> res)
    {
        ArrayList<Polynomial> list = spePrint(res);
        for (Polynomial polynomial : list)
        {
            BigInteger coefficient = polynomial.getCoefficient();
            if (coefficient.compareTo(BigInteger.ZERO) > 0)
            {
                System.out.print("+");
            }
            printCoe(coefficient, true);
            System.out.print("x**");
            System.out.print(polynomial.getIndex());
        }
    }

    /**
     * special print:
     * index of 0,1,2
     * if index start from above 2
     * then print the first one,
     * because of print will always print out '+' if coefficient is positive
     */
    private static ArrayList<Polynomial> spePrint(HashMap<BigInteger, Polynomial> res)
    {
        boolean flag = false;
        if (res.containsKey(BigInteger.ZERO))
        {
            Polynomial polynomial = res.get(BigInteger.ZERO);
            System.out.print(polynomial.getCoefficient());
            res.remove(BigInteger.ZERO);
            flag = true;
        }
        if (res.containsKey(BigInteger.ONE))
        {
            BigInteger coefficient = res.get(BigInteger.ONE).getCoefficient();
            if (flag && (coefficient.compareTo(BigInteger.ZERO) > 0))
            {
                System.out.print("+");
            }
            printCoe(coefficient, true);
            System.out.print("x");
            res.remove(BigInteger.ONE);
            flag = true;
        }
        if (res.containsKey(BigInteger.valueOf(2)))
        {
            BigInteger coefficient = res.get(BigInteger.valueOf(2)).getCoefficient();
            if (flag && (coefficient.compareTo(BigInteger.ZERO) > 0))
            {
                System.out.print("+");
            }
            printCoe(coefficient, true);
            System.out.print("x*x");
            res.remove(BigInteger.valueOf(2));
            flag = true;
        }
        ArrayList<Polynomial> list = new ArrayList<>(res.values());
        list.sort((o1, o2) -> o1.getIndex().compareTo(o2.getIndex()));
        if (!flag && !list.isEmpty())
        {
            Polynomial polynomial = list.get(0);
            printCoe(polynomial.getCoefficient(), true);
            System.out.print("x**");
            System.out.print(polynomial.getIndex());
            list.remove(0);
        }
        return list;
    }

    private static void printCoe(BigInteger coefficient, boolean isPrintStar)
    {
        if (!coefficient.equals(BigInteger.ONE))
        {
            if (coefficient.equals(BigInteger.valueOf(-1)))
            {
                System.out.print("-");
            }
            else
            {
                System.out.print(coefficient);
                if (isPrintStar)
                {
                    System.out.print("*");
                }
            }
        }
    }

}