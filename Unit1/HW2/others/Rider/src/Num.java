public class Num extends Operator {
    private String value;

    public Num(String value) {
        super(null, null);
        this.value = value;
    }

    public String getResult() {
        return value;
    }
}
