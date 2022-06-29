package exception;

import com.oocourse.spec2.exceptions.RelationNotFoundException;
import program.Counter;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private int id1;
    private int id2;
    private static Counter counter = new Counter();

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        counter.trigger(id1, id2);
    }

    @Override
    public void print() {
        int smallId = Math.min(id1, id2);
        int bigId = Math.max(id1, id2);
        System.out.println(String.format("rnf-%d, %d-%d, %d-%d", counter.getTriggerTotalTimes(),
                smallId, counter.checkTriggerTimes(smallId),
                bigId, counter.checkTriggerTimes(bigId)));
    }
}
