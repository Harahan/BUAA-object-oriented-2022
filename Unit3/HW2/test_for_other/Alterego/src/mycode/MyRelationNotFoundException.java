package mycode;

import com.oocourse.spec2.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private int id1;
    private int id2; //personId触发了异常
    private static int totalExcCnt = 0;
    private static HashMap<Integer, Integer> excTable = new HashMap<>();

    @Override
    public void print() {
        totalExcCnt++;
        if (excTable.containsKey(id1)) {
            excTable.put(id1, excTable.get(id1) + 1);
        } else {
            excTable.put(id1, 1);
        }
        if (excTable.containsKey(id2)) {
            excTable.put(id2, excTable.get(id2) + 1);
        } else {
            excTable.put(id2, 1);
        }
        System.out.printf("rnf-%d, %d-%d, %d-%d%n",
                totalExcCnt, id1, excTable.get(id1), id2, excTable.get(id2));
    }

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
    }
}
