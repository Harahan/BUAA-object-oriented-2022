public class Brancket extends Operator {
    public Brancket(Operator left, Operator right) {
        super(left, right);
    }

    public String getResult() {
        return getLeft().getResult();
    }
}
