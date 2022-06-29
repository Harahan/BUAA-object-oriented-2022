package parsing;

import java.util.ArrayList;

public class TRep extends Token {
    private final int atLeast;
    private final int atMost;
    private final Token token;

    public TRep(int atLeast, int atMost, Token token) {
        this.atLeast = atLeast;
        this.atMost = atMost;
        this.token = token;
    }

    public Result parse(Parser p) {
        p.push();

        ArrayList<Result> results = new ArrayList<>(this.atLeast);

        int i = 0;
        while (atMost < 0 || i <= atMost) {
            Result r = this.token.parse(p);
            if (r == null) {
                break;
            } else {
                results.add(r);
            }
            i++;
        }

        if (results.size() < this.atLeast) {
            return p.popFail(null);
        } else {
            return p.popOk(new Result(results));
        }
    }
}
