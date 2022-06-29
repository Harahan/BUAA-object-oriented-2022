public class Operator {
    private Operator left;
    private Operator right;

    public Operator(Operator left, Operator right) {
        this.left = left;
        this.right = right;
    }

    public String getResult() {
        return "";
    }

    public Operator getLeft() {
        return left;
    }

    public Operator getRight() {
        return right;
    }
}
