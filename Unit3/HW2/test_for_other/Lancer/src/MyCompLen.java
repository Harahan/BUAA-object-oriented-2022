import java.util.Comparator;

class MyCompLen implements Comparator<MyValue> {
    @Override
    public int compare(MyValue o1, MyValue o2) {
        return o1.getValue() - o2.getValue();
    }
}