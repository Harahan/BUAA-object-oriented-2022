package parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TList extends Token {
    private final List<Token> tokens;

    public TList(List<Token> tokens) {
        this.tokens = tokens;
    }

    public TList(Token... tokens) {
        this(Arrays.asList(tokens));
    }

    public Result parse(Parser p) {
        p.push();

        ArrayList<Result> results = new ArrayList<>(this.tokens.size());
        for (Token t : this.tokens) {
            Result r = t.parse(p);
            if (r == null) {
                return p.popFail(null);
            } else {
                results.add(r);
            }
        }

        return p.popOk(new Result(results));
    }
}
