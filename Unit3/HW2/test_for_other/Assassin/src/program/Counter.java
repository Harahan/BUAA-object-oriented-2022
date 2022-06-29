package program;

import java.util.HashMap;

public class Counter {
    private int triggerTotalTime = 0;
    private HashMap<Integer, Integer> triggerTimes = new HashMap<>();

    public void trigger(int id) {
        triggerTotalTime++;
        triggerTimes.put(id, triggerTimes.getOrDefault(id, 0) + 1);
    }

    public void trigger(int id1, int id2) {
        trigger(id1);
        if (id1 != id2) {
            triggerTimes.put(id2, triggerTimes.getOrDefault(id2, 0) + 1);
        }
    }

    public Integer getTriggerTotalTimes() {
        return triggerTotalTime;
    }

    public Integer checkTriggerTimes(int id) {
        return triggerTimes.getOrDefault(id, 0);
    }
}
