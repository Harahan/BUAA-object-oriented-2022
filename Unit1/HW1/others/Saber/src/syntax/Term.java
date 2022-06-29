package syntax;

import lex.Lexer;
import lex.UnknownTokenException;
import lex.WordAttr;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;

public class Term
{
    public static HashMap<BigInteger, Polynomial> calculate(Lexer lexer)
            throws UnknownTokenException, SyntaxException
    {
        WordAttr laAttr = lexer.lookAhead().getAttribute();
        boolean toNeg = false;
        if (laAttr == WordAttr.ADD || laAttr == WordAttr.SUB)
        {
            toNeg = laAttr == WordAttr.SUB;
            lexer.nextWord(); // take over + or -
        }
        HashMap<BigInteger, Polynomial> polynomials = Factor.calculate(lexer);
        WordAttr attribute = lexer.nextWord().getAttribute();
        while (attribute != WordAttr.ADD && attribute != WordAttr.SUB
                && attribute != WordAttr.END && attribute != WordAttr.RBRACKET)
        {
            if (attribute != WordAttr.MUL)
            {
                throw new SyntaxException("Term do not match");
            }
            polynomials = mul(polynomials, Factor.calculate(lexer));
            attribute = lexer.nextWord().getAttribute();
        }
        lexer.backwards();
        if (toNeg)
        {
            Expr.negate(polynomials);
        }
        return polynomials;
    }

    public static HashMap<BigInteger, Polynomial>
        mul(HashMap<BigInteger, Polynomial> left,
        HashMap<BigInteger, Polynomial> right)
    {
        HashMap<BigInteger, Polynomial> res = new HashMap<>();
        if (left.isEmpty() || right.isEmpty())
        {
            return res;
        }
        Collection<Polynomial> lvalues = left.values();
        Collection<Polynomial> rvalues = right.values();
        for (Polynomial l : lvalues)
        {
            for (Polynomial r : rvalues)
            {
                BigInteger coefficient = l.getCoefficient().multiply(r.getCoefficient());
                BigInteger index = l.getIndex().add(r.getIndex());
                if (!coefficient.equals(BigInteger.ZERO))
                {
                    // add up
                    if (res.containsKey(index))
                    {
                        Polynomial oldPol = res.get(index);
                        BigInteger newCoe = oldPol.getCoefficient().add(coefficient);
                        if (newCoe.equals(BigInteger.ZERO))
                        {
                            res.remove(index);
                        }
                        else
                        {
                            oldPol.setCoefficient(newCoe);
                        }
                    }
                    else
                    {
                        res.put(index, new Polynomial(coefficient, index));
                    }
                }
            }
        }
        return res;
    }
}
