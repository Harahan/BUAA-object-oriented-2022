import com.oocourse.spec2.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private int id1;
    private int id2;
    private static int times = 0;
    private static HashMap<Integer, Integer> person = new HashMap<>();

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        times++;
        if (id1 != id2) {
            if (person.containsKey(id1)) {
                int oldValue = person.get(id1);
                person.put(id1, oldValue + 1);
            } else {
                person.put(id1, 1);
            }
            if (person.containsKey(id2)) {
                int oldValue = person.get(id2);
                person.put(id2, oldValue + 1);
            } else {
                person.put(id2, 1);
            }
        } else {
            if (person.containsKey(id1)) {
                int oldValue = person.get(id1);
                person.put(id1, oldValue + 1);
            } else {
                person.put(id1, 1);
            }
        }
    }

    @Override
    public void print() {
        if (id1 <= id2) {
            System.out.println("er-" + times + ", " + id1 +
                    "-" + person.get(id1) + ", " + id2 + "-" + person.get(id2));
        } else {
            System.out.println("er-" + times + ", " + id2 +
                    "-" + person.get(id2) + ", " + id1 + "-" + person.get(id1));
        }
    }
}