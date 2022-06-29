package parsing;

import java.util.Arrays;
import java.util.List;

public class TSel extends Token {
    private final List<Token> tokens;

    public TSel(List<Token> tokens) {
        this.tokens = tokens;
    }

    public TSel(Token... tokens) {
        this(Arrays.asList(tokens));
    }

    public Result parse(Parser p) {
        int choiceNo = 0;
        for (Token t : this.tokens) {
            Result r = t.parse(p);
            if (r != null) {
                return new Result(choiceNo, r);
            }
            choiceNo++;
        }
        return null;
    }
}
