package myexceptions;

import com.oocourse.spec1.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> GROUPS = new HashMap<>();

    private final int id;

    public MyEqualGroupIdException(int id) {
        sum += 1;
        GROUPS.merge(id, 1, Integer::sum);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("egi-" + sum + ", " + id + "-" + GROUPS.get(id));
    }
}

