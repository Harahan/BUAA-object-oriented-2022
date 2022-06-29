package mycode;

import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private int id; //personId触发了异常
    private static int totalExcCnt = 0;
    private static HashMap<Integer, Integer> excTable = new HashMap<>();
    // 触发的personId, 触发的次数

    @Override
    public void print() {
        totalExcCnt++;
        if (excTable.containsKey(id)) {
            excTable.put(id, excTable.get(id) + 1);
        } else {
            excTable.put(id, 1);
        }
        System.out.printf("ginf-%d, %d-%d%n", totalExcCnt, id, excTable.get(id));
    }

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
    }
}
