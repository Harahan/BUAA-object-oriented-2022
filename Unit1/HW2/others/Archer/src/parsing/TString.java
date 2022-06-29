package parsing;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class TString extends Token {
    private final Pattern pattern;

    public TString(String string) {
        this(Pattern.compile(Pattern.quote(string)));
    }

    public TString(Pattern pattern) {
        this.pattern = pattern;
    }

    public static TString fromPatternString(String pattern) {
        return new TString(Pattern.compile(pattern));
    }

    public Result parse(Parser p) {
        MatchResult mr = p.matchRegex(this.pattern);
        if (mr == null) {
            return null;
        } else {
            return new Result(mr.group());
        }
    }
}
