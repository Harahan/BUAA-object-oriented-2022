import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private int id;
    private static int times = 0;
    private static HashMap<Integer, Integer> group = new HashMap<>();

    public MyEqualGroupIdException(int id) {
        this.id = id;
        times++;
        if (group.containsKey(id)) {
            int oldValue = group.get(id);
            group.put(id, oldValue + 1);
        } else {
            group.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("egi-" + times + ", " + id + "-" + group.get(id));
    }
}