package myexceptions;

import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> GROUPS = new HashMap<>();

    private final int id;

    public MyGroupIdNotFoundException(int id) {
        sum += 1;
        GROUPS.merge(id, 1, Integer::sum);
        this.id = id;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + sum + ", " + id + "-" + GROUPS.get(id));
    }
}