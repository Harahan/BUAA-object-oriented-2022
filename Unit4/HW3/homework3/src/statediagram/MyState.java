package statediagram;

import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MyState {
    private final String name;
    private boolean isFinal = false;

    private final HashMap<MyState, HashMap<String, String>> nextStatesWithTrigger = new HashMap<>();
    // myState, <id, name>
    private final HashMap<String, HashSet<String>> eventWithGuard = new HashMap<>();
    // eventName, guardName
    private boolean checkRule009 = true;

    private boolean checkRule008 = true;

    private final HashSet<String> emptyGuard = new HashSet<>();

    public MyState(UmlState umlState) {
        name = umlState.getName();
    }

    public MyState(UmlFinalState umlFinalState) {
        name = umlFinalState.getName();
        isFinal = true;
    }

    public MyState(UmlPseudostate umlPseudostate) {
        name = umlPseudostate.getName();
    }

    public String getName() {
        return name;
    }

    public Set<MyState> getNextStates() {
        return nextStatesWithTrigger.keySet();
    }

    public void addTransition(MyState nextState) {
        if (isFinal) {
            checkRule008 = false;
        }
        nextStatesWithTrigger.merge(nextState, new HashMap<>(), (oldList, newList) -> oldList);
    }

    public void addTrigger(MyState nextState, String id, String name, String guard) {
        nextStatesWithTrigger.get(nextState).put(id, name);
        if (checkRule009) {
            checkEvent(name, guard);
        }
    }

    public ArrayList<String> getTriggerNames(MyState nextStates) {
        HashMap<String, String> tmpMap = nextStatesWithTrigger.getOrDefault(nextStates, null);
        return tmpMap == null ? null : new ArrayList<String>() {{
                tmpMap.forEach((id, name) -> add(name));
            }};
    }

    private void checkEvent(String event, String guard) {
        if (eventWithGuard.containsKey(event) && (guard == null || Pattern.matches("[ \\t]*", guard)
                || eventWithGuard.get(event).contains(guard) || emptyGuard.contains(event))) {
            checkRule009 = false;
            return;
        }
        eventWithGuard.merge(event, new HashSet<String>() {{
                add(guard);
            }}, (oldSet, newSet) -> {
                oldSet.add(guard);
                return oldSet;
            });
        if (guard == null || Pattern.matches("[ \\t]*", guard)) {
            emptyGuard.add(event);
        }
    }

    public boolean isCheckRule009() {
        return checkRule009;
    }

    public boolean isCheckRule008() {
        return checkRule008;
    }
}
