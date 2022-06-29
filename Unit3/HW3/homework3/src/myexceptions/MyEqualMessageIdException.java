package myexceptions;

import com.oocourse.spec3.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> MESSAGES = new HashMap<>();

    private final int id;

    public MyEqualMessageIdException(int id) {
        sum += 1;
        MESSAGES.merge(id, 1, Integer::sum);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("emi-" + sum + ", " + id + "-" + MESSAGES.get(id));
    }
}
