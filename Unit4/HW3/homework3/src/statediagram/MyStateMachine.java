package statediagram;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.models.elements.UmlStateMachine;

import java.util.List;

public class MyStateMachine {
    private MyRegion myRegion;
    private final String name;

    public MyStateMachine(UmlStateMachine umlStateMachine) {
        name = umlStateMachine.getName();
    }

    public void setMyRegion(MyRegion myRegion) {
        this.myRegion = myRegion;
        myRegion.setStateMachineName(name);
    }

    public int getStateNum() {
        return myRegion.getStateNum();
    }

    public boolean getStateIsCriticalPoint(String name) throws
            StateNotFoundException, StateDuplicatedException {
        return myRegion.getStateIsCriticalPoint(name);
    }

    public List<String> getTransitionTrigger(String s1, String s2) throws
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return myRegion.getTransitionTrigger(s1, s2);
    }

    public boolean isCheckRule009() {
        return myRegion.isCheckRule009();
    }

    public boolean isCheckRule008() {
        return myRegion.isCheckRule008();
    }
}
