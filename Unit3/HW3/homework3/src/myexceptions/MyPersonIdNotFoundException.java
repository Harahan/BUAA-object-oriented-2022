package myexceptions;

import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> PEOPLE = new HashMap<>();

    private final int id;

    public MyPersonIdNotFoundException(int id) {
        sum += 1;
        PEOPLE.merge(id, 1, Integer::sum);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("pinf-" + sum + ", " + id + "-" + PEOPLE.get(id));
    }
}
