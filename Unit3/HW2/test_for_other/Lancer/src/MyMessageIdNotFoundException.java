import com.oocourse.spec2.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static int times = 0;
    private static HashMap<Integer, Integer> message = new HashMap<>();

    public MyMessageIdNotFoundException(int id) {
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
        System.out.println("minf-" + times + ", " + id + "-" + message.get(id));
    }
}
