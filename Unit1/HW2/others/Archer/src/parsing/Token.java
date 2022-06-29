package parsing;

public class Token {
    public Token() {
        // throw new RuntimeException("trying to use an abstract Token");
    }

    public Result parse(Parser p) {
        throw new RuntimeException("trying to use an abstract Token");
    }

    public TMark marker() {
        return new TMark(this);
    }

    public TRep rep() {
        return this.rep(0);
    }

    public TRep rep(int atLeast) {
        return new TRep(atLeast, -1, this);
    }

    public TRep rep(int atLeast, int atMost) {
        return new TRep(atLeast, atMost, this);
    }

    public TOpt opt() {
        return new TOpt(this);
    }
}
