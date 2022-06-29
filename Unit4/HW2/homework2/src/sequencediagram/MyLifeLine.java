package sequencediagram;

import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.models.elements.UmlLifeline;

public class MyLifeLine {
    private final UmlLifeline umlLifeline;
    private String interactionName;
    private boolean error = false;
    private MyLifeLine creator = null;
    private int foundNum = 0;
    private int lostNum = 0;

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
}
