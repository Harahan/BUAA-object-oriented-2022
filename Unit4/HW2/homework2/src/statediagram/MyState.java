package statediagram;

import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MyState {
    private final String name;

    private final HashMap<MyState, HashMap<String, String>> nextStatesWithTrigger = new HashMap<>();

    public MyState(UmlState umlState) {
        name = umlState.getName();
    }

    public MyState(UmlFinalState umlFinalState) {
        name = umlFinalState.getName();
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
        nextStatesWithTrigger.merge(nextState, new HashMap<>(), (oldList, newList) -> oldList);
    }

    public void addTrigger(MyState nextState, String id, String name) {
        nextStatesWithTrigger.get(nextState).put(id, name);
    }

    public ArrayList<String> getTriggerNames(MyState nextStates) {
        HashMap<String, String> tmpMap = nextStatesWithTrigger.getOrDefault(nextStates, null);
        return tmpMap == null ? null : new ArrayList<String>() {{
                tmpMap.forEach((id, name) -> add(name));
            }};
    }
}
