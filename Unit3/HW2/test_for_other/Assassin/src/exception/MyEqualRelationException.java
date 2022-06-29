package exception;

import com.oocourse.spec2.exceptions.EqualRelationException;
import program.Counter;

public class MyEqualRelationException extends EqualRelationException {
    private int id1;
    private int id2;
    private static Counter counter = new Counter();

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        counter.trigger(id1, id2);
    }

    @Override
    public void print() {
        int smallId = Math.min(id1, id2);
        int bigId = Math.max(id1, id2);
        System.out.println(String.format("er-%d, %d-%d, %d-%d",
                counter.getTriggerTotalTimes(), smallId, counter.checkTriggerTimes(smallId),
                bigId, counter.checkTriggerTimes(bigId)));
    }
}
