package statediagram;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlTransition;
import com.oocourse.uml2.models.elements.UmlEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateDiagram {
    private final HashMap<String, Object> myElements;
    private final HashMap<String, ArrayList<MyStateMachine>> myStateMachines = new HashMap<>();
    // name, arrayList<myStateMachine>()

    public StateDiagram(HashMap<String, Object> myElements) {
        this.myElements = myElements;
    }

    public void parseUmlStateMachine(UmlStateMachine umlStateMachine) {
        ArrayList<MyStateMachine> tmpList = new ArrayList<MyStateMachine>() {{
                add(new MyStateMachine(umlStateMachine));
            }};
        myElements.put(umlStateMachine.getId(), tmpList.get(0));
        myStateMachines.merge(umlStateMachine.getName(), tmpList, (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        });
    }

    public void parseUmlRegion(UmlRegion umlRegion) {
        MyRegion myRegion = new MyRegion();
        myElements.put(umlRegion.getId(), myRegion);
        ((MyStateMachine) myElements.get(umlRegion.getParentId())).setMyRegion(myRegion);
    }

    public void parseUmlState(UmlState umlState) {
        MyState myState = new MyState(umlState);
        myElements.put(umlState.getId(), myState);
        ((MyRegion) myElements.get(umlState.getParentId())).addState(myState);
    }

    public void parseUmlFinalState(UmlFinalState umlFinalState) {
        MyState myState = new MyState(umlFinalState);
        myElements.put(umlFinalState.getId(), myState);
        ((MyRegion) myElements.get(umlFinalState.getParentId())).addFinalState(myState);
    }

    public void parseUmlPseudoState(UmlPseudostate umlPseudostate) {
        MyState myState = new MyState(umlPseudostate);
        myElements.put(umlPseudostate.getId(), myState);
        ((MyRegion) myElements.get(umlPseudostate.getParentId())).setInitialState(myState);
    }

    public void parseUmlTransition(UmlTransition umlTransition) {
        myElements.put(umlTransition.getId(), umlTransition);
        ((MyState) myElements.get(umlTransition.getSource())).addTransition(
                (MyState) myElements.get(umlTransition.getTarget()));
    }

    public void parseUmlEvent(UmlEvent umlEvent) {
        UmlTransition umlTransition = ((UmlTransition) myElements.get(umlEvent.getParentId()));
        ((MyState) myElements.get(umlTransition.getSource())).addTrigger(
                (MyState) myElements.get(umlTransition.getTarget()),
                umlEvent.getId(), umlEvent.getName());
    }

    public int getStateCount(String s) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return getStateMachine(s).getStateNum();
    }

    public boolean getStateIsCriticalPoint(String s, String s1) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return getStateMachine(s).getStateIsCriticalPoint(s1);
    }

    public List<String> getTransitionTrigger(String s, String s1, String s2) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return getStateMachine(s).getTransitionTrigger(s1, s2);
    }

    private MyStateMachine getStateMachine(String name) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        ArrayList<MyStateMachine> stateMachines = myStateMachines.getOrDefault(name, null);
        if (stateMachines == null) {
            throw new StateMachineNotFoundException(name);
        } else if (stateMachines.size() > 1) {
            throw new StateMachineDuplicatedException(name);
        }
        return stateMachines.get(0);
    }
}
