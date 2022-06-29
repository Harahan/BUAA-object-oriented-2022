package expr;

import java.math.BigInteger;

public class Trigo implements Factor {
    private int mode;
    private Variable content;
    private BigInteger index;
    private boolean merge;

    public Trigo(int mode, Variable content, BigInteger index) {
        this.mode = mode;
        this.content = content;
        this.index = index;
        this.merge = true;
    }

    public Trigo(Trigo trigo) {
        mode = trigo.mode;
        content = trigo.content;
        index = trigo.index;
        merge = trigo.merge;
    }

    public int getMode() {
        return mode;
    }

    public boolean isMerge() {
        return merge;
    }

    public void merge(Trigo trigo) {
        if (mode == trigo.mode && content.equal(trigo.content)) {
            index = index.add(trigo.index);
            trigo.merge = false;
            merge = false;
        }
    }

    public Variable getContent() {
        return content;
    }

    public boolean equals(Trigo trigo) {
        return mode == trigo.mode && content.equal(trigo.content) && index.equals(trigo.index);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (mode == 1) {
            sb.append("sin(");
        } else {
            sb.append("cos(");
        }
        if (content.getIndex() == 0) {
            sb.append(content.getCoefficient().abs() + ")");
        } else if (content.getIndex() == 1) {
            sb.append("x)");
        } else {
            sb.append("x**" + content.getIndex() + ")");
        }
        if (!index.equals(BigInteger.ONE)) {
            sb.append("**" + index);
        }
        return sb.toString();
    }
}
