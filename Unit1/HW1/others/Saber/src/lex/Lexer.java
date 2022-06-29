package lex;

import java.math.BigInteger;

/**
 * All words' attribute seen in Enum WordAttr
 */

public class Lexer
{
    private final String expr;
    private int ptr = 0;
    private int shadow = 0;

    public class Entry
    {
        private final WordAttr attribute;
        private final Object value;

        public Entry(WordAttr attribute, Object value)
        {
            this.attribute = attribute;
            this.value = value;
        }

        public WordAttr getAttribute()
        {
            return attribute;
        }

        public Object getValue()
        {
            return value;
        }
    }

    public Lexer(String expr)
    {
        this.expr = expr.trim();
    }

    public Lexer.Entry nextWord() throws UnknownTokenException
    {
        shadow = ptr;
        if (ptr >= expr.length())
        {
            return new Entry(WordAttr.END, null);
        }
        char ch = expr.charAt(ptr);
        if (Character.isWhitespace(ch))
        {
            do
            {
                ch = expr.charAt(++ptr);
            } while (Character.isWhitespace(ch));
        }

        if (Character.isDigit(ch))
        {
            return this.checkDigit();
        }
        else if (Character.isLetter(ch))
        {
            return this.checkIdentify();
        }
        else
        {
            return this.checkSymbol(ch);
        }
    }

    private Lexer.Entry checkDigit()
    {
        int start = ptr;
        char ch;
        do
        {
            ++ptr;
            if (ptr >= expr.length())
            {
                break;
            }
            ch = expr.charAt(ptr);
        } while (Character.isDigit(ch));
        BigInteger value = new BigInteger(expr.substring(start, ptr));
        return new Entry(WordAttr.NUM, value);
    }

    private Lexer.Entry checkIdentify()
    {
        int start = ptr;
        char ch;
        do
        {
            ++ptr;
            if (ptr >= expr.length())
            {
                break;
            }
            ch = expr.charAt(ptr);
        } while (Character.isDigit(ch) || Character.isLetter(ch));
        return new Entry(WordAttr.IDENTIFY, expr.substring(start, ptr));
    }

    private Lexer.Entry checkSymbol(char ch) throws UnknownTokenException
    {
        WordAttr attribute;
        switch (ch)
        {
            case '(':
                attribute = WordAttr.LBRACKET;
                break;
            case ')':
                attribute = WordAttr.RBRACKET;
                break;
            case '+':
                attribute = WordAttr.ADD;
                break;
            case '-':
                attribute = WordAttr.SUB;
                break;
            case '*':
                if (ptr + 1 < expr.length() && expr.charAt(ptr + 1) == '*')
                {
                    ++ptr;
                    attribute = WordAttr.DSTAR;
                }
                else
                {
                    attribute = WordAttr.MUL;
                }
                break;
            default:
                throw new UnknownTokenException("Unknown token: " + ch);
        }
        ++ptr;
        return new Entry(attribute, null);
    }

    public Lexer.Entry lookAhead()
    {
        Entry tem;
        try
        {
            tem = this.nextWord();
        } catch (UnknownTokenException e)
        {
            tem = new Entry(WordAttr.END, null);
        }
        ptr = shadow;
        return tem;
    }

    public boolean backwards()
    {
        if (shadow == ptr)
        {
            return false;
        }
        ptr = shadow;
        return true;
    }

}
