package myexceptions;

import com.oocourse.spec2.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int sum = 0;
    private static final HashMap<Integer, Integer> RELATION = new HashMap<>();

    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        sum += 1;
        RELATION.merge(id1, 1, Integer::sum);
        RELATION.merge(id2, 1, Integer::sum);
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
    }

    @Override
    public void print() {
        System.out.println("rnf-" + sum + ", " + id1 + "-" + RELATION.get(id1) +
                ", " + id2 + "-" + RELATION.get(id2));
    }
}
