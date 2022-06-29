package exception;

import com.oocourse.spec2.exceptions.EqualPersonIdException;
import program.Counter;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static Counter counter = new Counter();
    private int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        MyEqualPersonIdException.counter.trigger(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("epi-%d, %d-%d",
                counter.getTriggerTotalTimes(), id, counter.checkTriggerTimes(id)));
    }
}
