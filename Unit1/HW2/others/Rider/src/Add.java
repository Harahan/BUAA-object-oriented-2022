public class Add extends Operator {
    public Add(Operator left, Operator right) {
        super(left, right);
    }

    public String getResult() {
        return getLeft().getResult() + "+" + getRight().getResult();
    }
}