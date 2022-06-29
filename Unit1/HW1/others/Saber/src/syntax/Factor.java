package syntax;

import lex.Lexer;
import lex.UnknownTokenException;
import lex.WordAttr;

import java.math.BigInteger;
import java.util.HashMap;

public class Factor
{
    public static HashMap<BigInteger, Polynomial> calculate(Lexer lexer)
            throws UnknownTokenException, SyntaxException
    {
        HashMap<BigInteger, Polynomial> polynomials;
        Lexer.Entry word = lexer.nextWord();
        WordAttr attribute = word.getAttribute();
        if (attribute == WordAttr.LBRACKET) // '(' EXP ')'[** INDEX]
        {
            HashMap<BigInteger, Polynomial> exprVal = Expr.calculate(lexer);
            // ')' has been taken by Expr
            BigInteger index = getIndex(lexer);
            polynomials = power(exprVal, index);
        }
        else if (attribute == WordAttr.IDENTIFY) // 'x'[** INDEX]
        {
            polynomials = new HashMap<>();
            BigInteger index = getIndex(lexer);
            polynomials.put(index, new Polynomial(BigInteger.ONE, index));
        }
        else // NUM or error
        {
            lexer.backwards();
            BigInteger intVal = getNum(lexer);
            polynomials = new HashMap<>();
            if (!intVal.equals(BigInteger.ZERO))
            {
                polynomials.put(BigInteger.ZERO, new Polynomial(intVal, BigInteger.ZERO));
            }
        }
        return polynomials;
    }

    private static BigInteger getIndex(Lexer lexer)
            throws UnknownTokenException, SyntaxException
    {
        if (lexer.nextWord().getAttribute() == WordAttr.DSTAR)
        {
            return getNum(lexer);
        }
        else
        {
            lexer.backwards();
            return BigInteger.ONE;
        }
    }

    private static BigInteger getNum(Lexer lexer)
            throws UnknownTokenException, SyntaxException
    {
        Lexer.Entry word = lexer.nextWord();
        WordAttr attribute = word.getAttribute();
        boolean toNeg = false;
        if (attribute == WordAttr.ADD || attribute == WordAttr.SUB)
        {
            toNeg = attribute == WordAttr.SUB;
            word = lexer.nextWord();
            attribute = word.getAttribute();
        }
        if (attribute != WordAttr.NUM)
        {
            throw new SyntaxException("Factor do not match");
        }

        BigInteger intVal = (BigInteger) word.getValue();
        intVal = toNeg ? intVal.negate() : intVal;
        return intVal;
    }

    public static HashMap<BigInteger, Polynomial>
        power(HashMap<BigInteger, Polynomial> polynomials,
          BigInteger index)
    {
        HashMap<BigInteger, Polynomial> res = polynomials;
        // make a convention that 0th power of 0 equals 1
        if (index.equals(BigInteger.ZERO))
        {
            res = new HashMap<>();
            res.put(BigInteger.ZERO, new Polynomial(BigInteger.ONE, BigInteger.ZERO));
            return res;
        }
        if (polynomials.isEmpty())
        {
            // if index < 0 throw exception
            res = new HashMap<>();
            return res;
        }
        BigInteger copied = index;
        while (!copied.equals(BigInteger.ONE))
        {
            res = Term.mul(polynomials, res);
            copied = copied.subtract(BigInteger.ONE);
        }
        return res;
    }
}
