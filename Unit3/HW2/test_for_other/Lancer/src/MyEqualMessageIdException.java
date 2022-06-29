import com.oocourse.spec2.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private int id;
    private static int times = 0;
    private static HashMap<Integer, Integer> message = new HashMap<>();

    public MyEqualMessageIdException(int id) {
        this.id = id;
        times++;
        if (message.containsKey(id)) {
            int oldValue = message.get(id);
            message.put(id, oldValue + 1);
        } else {
            message.put(id, 1);
        }
    }

    @Override
    public void print() {
        System.out.println("emi-" + times + ", " + id + "-" + message.get(id));
    }
}
