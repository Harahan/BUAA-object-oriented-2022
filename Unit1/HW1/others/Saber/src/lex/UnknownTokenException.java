package lex;

public class UnknownTokenException extends Exception
{
    public UnknownTokenException()
    {
        super();
    }

    public UnknownTokenException(String message)
    {
        super(message);
    }
}
