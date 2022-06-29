import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private int id;
    private static int times = 0;
    private static HashMap<Integer, Integer> person = new HashMap<>();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        times++;
        if (person.containsKey(id)) {
            int oldValue = person.get(id);
            person.put(id, oldValue + 1);
        } else {
            person.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("pinf-" + times + ", " + id + "-" + person.get(id));
    }
}
