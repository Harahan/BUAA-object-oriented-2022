package parsing;

public class TMark extends Token {
    private final Token token;

    public TMark(Token token) {
        this.token = token;
    }

    public Result parse(Parser p) {
        if (this.token.parse(p) == null) {
            return null;
        }
        return new Result();
    }
}
