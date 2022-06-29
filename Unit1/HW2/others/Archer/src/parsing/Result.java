package parsing;

import java.util.ArrayList;
import java.util.List;

public class Result {
    public enum Type {
        // Empty => [ ] or some special token
        // String => T
        // Results => A B C ... or { ... }
        // Choices => A | B | C
        // Custom => Others
        Empty, String, ResultList, Choices, Custom
    }

    private final Type type;
    private final int choiceNo;
    private final Object value;

    public Result() {
        this.type = Type.Empty;
        this.choiceNo = 0;
        this.value = null;
    }

    public Result(String content) {
        this.type = Type.String;
        this.choiceNo = 0;
        this.value = content;
    }

    public Result(List<Result> content) {
        this.type = Type.ResultList;
        this.choiceNo = 0;
        this.value = content;
    }

    public Result(int choiceNo, Result content) {
        this.type = Type.Choices;
        this.choiceNo = choiceNo;
        this.value = content;
    }

    public Result(Object content) {
        this.type = Type.Custom;
        this.choiceNo = 0;
        this.value = content;
    }

    public Type getType() {
        return this.type;
    }

    public String getString() {
        if (this.type == Type.String) {
            return (String) this.value;
        }
        throw new RuntimeException("try to get wrong type.");
    }

    // SAFETY: see constructor
    @SuppressWarnings("unchecked")
    public List<Result> getResultList() {
        if (this.type == Type.ResultList) {
            return (List<Result>) this.value;
        }
        throw new RuntimeException("try to get wrong type.");
    }

    public int getChoiceNo() {
        if (this.type == Type.Choices) {
            return this.choiceNo;
        }
        throw new RuntimeException("try to get wrong type.");
    }

    public Result getChoice() {
        if (this.type == Type.Choices) {
            return (Result) this.value;
        }
        throw new RuntimeException("try to get wrong type.");
    }

    public Object getCustom() {
        if (this.type == Type.Custom) {
            return this.value;
        }
        throw new RuntimeException("try to get wrong type.");
    }

    public String toString() {
        switch (this.type) {
            case Empty:
                return "";
            case String:
                // return "S(" + this.getString() + ")";
                return this.getString();
            case ResultList: {
                List<Result> results = this.getResultList();
                ArrayList<String> strings = new ArrayList<>(results.size());
                for (Result r : results) {
                    strings.add(r.toString());
                }
                // return "L(" + String.join(", ", strings) + ")";
                return String.join("", strings);
            }
            case Choices:
                // return String.format("X(%d %s)", this.getChoiceNo(),
                // this.getChoice().toString());
                return this.getChoice().toString();
            case Custom:
                // return "C(" + this.getCustom().toString() + ")";
                return this.getCustom().toString();
            default:
                return null; // impossible
        }
    }
}
