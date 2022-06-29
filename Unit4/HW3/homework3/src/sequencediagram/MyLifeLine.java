package sequencediagram;

import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

public class MyLifeLine {
    private final UmlLifeline umlLifeline;
    private String interactionName;
    private boolean error = false;
    private MyLifeLine creator = null;
    private int foundNum = 0;
    private int lostNum = 0;

    private boolean isAlive = true;

    private boolean checkRule007 = true;

    public MyLifeLine(UmlLifeline umlLifeline) {
        this.umlLifeline = umlLifeline;
    }

    public String getName() {
        return umlLifeline.getName();
    }

    public void setInteractionName(String name) {
        interactionName = name;
    }

    public void setCreator(MyLifeLine other) {
        if (creator != null) {
            error = true;
        }
        creator = other;
    }

    public void addFoundNum() {
        ++foundNum;
    }

    public void addLostNum() {
        ++lostNum;
    }

    public Pair<Integer, Integer> getLostAndFound() {
        return new Pair<>(foundNum, lostNum); // found before lost
    }

    public UmlLifeline getCreator() throws
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (creator == null) {
            throw new LifelineNeverCreatedException(interactionName, umlLifeline.getName());
        } else if (error) {
            throw new LifelineCreatedRepeatedlyException(interactionName, umlLifeline.getName());
        }
        return creator.umlLifeline;
    }

    public void checkAlive(UmlMessage umlMessage) {
        if (umlMessage.getMessageSort() == MessageSort.DELETE_MESSAGE && isAlive) {
            isAlive = false;
        } else if (!isAlive) {
            checkRule007 = false;
        }
    }

    public boolean isCheckRule007() {
        return checkRule007;
    }
}
