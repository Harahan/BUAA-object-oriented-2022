package parsing;

import java.util.Stack;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final static Pattern pattern = Pattern.compile(".");

    // private final String content;
    private final Matcher matcher;
    private final Stack<Integer> stack;

    public Parser(String content) {
        // this.content = content;
        this.matcher = Parser.pattern.matcher(content);
        this.stack = new Stack<>();

        this.stack.push(0);
    }

    public int peek() {
        return this.stack.peek();
    }

    public boolean ended() {
        this.matcher.region(this.stack.peek(), this.matcher.regionEnd());
        return this.matcher.hitEnd();
    }

    public void push() {
        int index = this.stack.peek();
        this.stack.push(index);
    }

    public <T> T popOk(T retVal) {
        int index = this.stack.pop();
        this.stack.pop();
        this.stack.push(index);
        return retVal;
    }

    public <T> T popFail(T retVal) {
        // System.err.println(this.stack.peek());
        this.stack.pop();
        return retVal;
    }

    public MatchResult matchRegex(Pattern r) {
        this.matcher.region(this.stack.peek(), this.matcher.regionEnd());
        this.matcher.usePattern(r);

        if (this.matcher.lookingAt()) {
            this.stack.pop();
            this.stack.push(this.matcher.end());
            return this.matcher.toMatchResult();
        }

        return null;
    }
}
