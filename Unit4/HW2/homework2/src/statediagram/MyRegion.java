package statediagram;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class MyRegion {
    private MyState initialState;
    private String stateMachineName;
    private HashSet<MyState> arrivalFinalStates;
    private HashSet<MyState> vis;
    private final HashSet<MyState> finalStates = new HashSet<>();
    private final HashMap<String, ArrayList<MyState>> totStates = new HashMap<>();
    // name, hashSet<myState>()
    private int stateNum = 0;

    public void addState(MyState myState) {
        ++stateNum;
        totStates.merge(myState.getName(), new ArrayList<MyState>() {{
                add(myState);
            }}, (oldList, newList) -> {
                oldList.add(myState);
                return oldList;
            });
    }

    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }

    public void setInitialState(MyState myState) {
        initialState = myState;
        addState(myState);
    }

    public void addFinalState(MyState myState) {
        finalStates.add(myState);
        addState(myState);
    }

    public int getStateNum() {
        return stateNum;
    }

    public boolean getStateIsCriticalPoint(String name) throws
            StateNotFoundException, StateDuplicatedException {
        arrivalFinalStates = new HashSet<>();
        vis = new HashSet<>();
        getArrivalFinalStates(initialState, null);
        vis = new HashSet<>();
        MyState tag = getState(name); // be careful throwing exception first
        if (arrivalFinalStates.isEmpty()) {
            return false;
        }
        arrivalFinalStates = new HashSet<>();
        getArrivalFinalStates(initialState, tag);
        return arrivalFinalStates.isEmpty();
    }

    private void getArrivalFinalStates(MyState pre, MyState tag) {
        if (finalStates.contains(pre)) {
            arrivalFinalStates.add(pre);
            return;
        }
        for (MyState nxt : pre.getNextStates()) {
            if (nxt != tag && !vis.contains(nxt)) {
                vis.add(nxt);
                getArrivalFinalStates(nxt, tag);
            }
        }
    }

    public List<String> getTransitionTrigger(String s1, String s2) throws
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        MyState source = getState(s1);
        MyState target = getState(s2);
        List<String> rt = source.getTriggerNames(target);
        if (rt == null) {
            throw new TransitionNotFoundException(stateMachineName, s1, s2);
        }
        return rt;
    }

    private MyState getState(String name) throws
            StateNotFoundException, StateDuplicatedException {
        ArrayList<MyState> states = totStates.getOrDefault(name, null);
        if (states == null) {
            throw new StateNotFoundException(stateMachineName, name);
        } else if (states.size() > 1) {
            throw new StateDuplicatedException(stateMachineName, name);
        }
        return states.get(0);
    }

}
