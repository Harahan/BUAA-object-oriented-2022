package syntax;

import lex.Lexer;
import lex.UnknownTokenException;
import lex.WordAttr;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Expr
{
    public static HashMap<BigInteger, Polynomial> calculate(Lexer lexer)
            throws UnknownTokenException, SyntaxException
    {
        HashMap<BigInteger, Polynomial> polynomials;
        WordAttr laAttr = lexer.lookAhead().getAttribute();
        boolean toNeg = false;
        if (laAttr == WordAttr.ADD || laAttr == WordAttr.SUB)
        {
            toNeg = laAttr == WordAttr.SUB;
            lexer.nextWord(); // take over + or -
        }
        polynomials = Term.calculate(lexer);
        if (toNeg)
        {
            negate(polynomials);
        }
        WordAttr attribute = lexer.nextWord().getAttribute();
        while (attribute != WordAttr.END && attribute != WordAttr.RBRACKET)
        {
            if (attribute != WordAttr.ADD && attribute != WordAttr.SUB)
            {
                throw new SyntaxException("Expr do not match");
            }
            addSub(attribute == WordAttr.ADD, polynomials, Term.calculate(lexer));
            attribute = lexer.nextWord().getAttribute();
        }
        return polynomials;
    }

    public static void addSub(boolean isAdd, HashMap<BigInteger, Polynomial> left,
                              HashMap<BigInteger, Polynomial> right)
    {
        Set<Map.Entry<BigInteger, Polynomial>> rightEntry = right.entrySet();
        for (Map.Entry<BigInteger, Polynomial> r : rightEntry)
        {
            if (left.containsKey(r.getKey()))
            {
                Polynomial poly = left.get(r.getKey());
                BigInteger tem = isAdd ? poly.getCoefficient().add(r.getValue().getCoefficient()) :
                        poly.getCoefficient().subtract(r.getValue().getCoefficient());
                if (tem.equals(BigInteger.ZERO))
                {
                    left.remove(r.getKey());
                }
                else
                {
                    poly.setCoefficient(tem);
                }
            }
            else
            {
                BigInteger coefficient = r.getValue().getCoefficient();
                coefficient = isAdd ? coefficient : coefficient.negate();
                left.put(r.getKey(), new Polynomial(coefficient, r.getKey()));
            }
        }
    }

    public static void negate(HashMap<BigInteger, Polynomial> polynomials)
    {
        Collection<Polynomial> collection = polynomials.values();
        for (Polynomial p : collection)
        {
            p.setCoefficient(p.getCoefficient().negate());
        }
    }

}
