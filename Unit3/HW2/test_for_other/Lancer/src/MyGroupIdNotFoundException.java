import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private int id;
    private static int times = 0;
    private static HashMap<Integer, Integer> group = new HashMap<>();

    public MyGroupIdNotFoundException(int id) {
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
        System.out.println("ginf-" + times + ", " + id + "-" + group.get(id));
    }
}