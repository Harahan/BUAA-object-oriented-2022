package parsing;

public class TOpt extends Token {
    private final Token token;

    public TOpt(Token token) {
        this.token = token;
    }

    public Result parse(Parser p) {
        Result r = this.token.parse(p);
        if (r == null) {
            return new Result();
        } else {
            return r;
        }
    }
}
