package myexceptions;

import com.oocourse.spec3.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> PEOPLE = new HashMap<>();

    private final int id;

    public MyEqualPersonIdException(int id) {
        sum += 1;
        PEOPLE.merge(id, 1, Integer::sum);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("epi-" + sum + ", " + id + "-" + PEOPLE.get(id));
    }
}
