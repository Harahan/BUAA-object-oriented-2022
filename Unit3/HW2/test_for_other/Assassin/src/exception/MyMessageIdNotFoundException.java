package exception;

import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import program.Counter;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static Counter counter = new Counter();

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        this.counter.trigger(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("minf-%d, %d-%d",
                counter.getTriggerTotalTimes(), id, counter.checkTriggerTimes(id)));
    }
}
