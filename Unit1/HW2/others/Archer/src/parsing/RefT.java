package parsing;

public class RefT extends Token {
    private boolean set;
    private Token token;

    public RefT() {
        this.set = false;
        this.token = null;
    }

    public void setToken(Token token) {
        if (this.set) {
            throw new RuntimeException("trying to set RefT the second time.");
        }
        this.set = true;
        this.token = token;
    }

    public void setTokenList(Token... tokens) {
        this.setToken(new TList(tokens));
    }

    public void setTokenSel(Token... tokens) {
        this.setToken(new TSel(tokens));
    }

    public Token getToken() {
        return this.token;
    }

    public Result parse(Parser p) {
        if (this.token == null) {
            throw new RuntimeException("trying to use an empty Token reference");
        }
        return this.token.parse(p);
    }
}
