package sequencediagram;

import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class SequenceDiagram {
    private final HashMap<String, Object> myElements;
    private final HashMap<String, ArrayList<MyInteraction>> myInteractions = new HashMap<>();

    private final HashSet<MyInteraction> myInteractionsForCheck = new HashSet<>();

    private boolean checkRule006 = true;

    public SequenceDiagram(HashMap<String, Object> myElements) {
        this.myElements = myElements;
    }

    public void parseUmlInteraction(UmlInteraction umlInteraction) {
        ArrayList<MyInteraction> tmpList = new ArrayList<MyInteraction>() {{
                add(new MyInteraction(umlInteraction));
            }};
        myElements.put(umlInteraction.getId(), tmpList.get(0));
        myInteractionsForCheck.add(tmpList.get(0));
        myInteractions.merge(umlInteraction.getName(), tmpList, (oldList, newList) -> {
            oldList.addAll(newList);
            return oldList;
        });
    }

    public void parseUmlIifeLine(UmlLifeline umlLifeline) {
        MyLifeLine myLifeLine = new MyLifeLine(umlLifeline);
        myElements.put(umlLifeline.getId(), myLifeLine);
        if (checkRule006) {
            checkAttribute(umlLifeline);
        }
        ((MyInteraction) myElements.get(umlLifeline.getParentId())).addLifLine(myLifeLine);
    }

    public void parseUmlEndpoint(UmlEndpoint umlEndpoint) {
        myElements.put(umlEndpoint.getId(), umlEndpoint);
    }

    public void parseUmlMessage(UmlMessage umlMessage) {
        Object source = myElements.get(umlMessage.getSource());
        Object target = myElements.get(umlMessage.getTarget());
        MessageSort messageSort = umlMessage.getMessageSort();
        // source - endpoint, target - lifeLine ---> not exist
        if (messageSort == MessageSort.CREATE_MESSAGE
                && target instanceof MyLifeLine && source instanceof MyLifeLine) {
            ((MyLifeLine) target).setCreator((MyLifeLine) source);
        }
        if (source instanceof UmlEndpoint && target instanceof MyLifeLine) {
            ((MyLifeLine) target).addFoundNum();
        } else if (source instanceof MyLifeLine && target instanceof UmlEndpoint) {
            ((MyLifeLine) source).addLostNum();
        }
        if (target instanceof MyLifeLine) {
            ((MyLifeLine) target).checkAlive(umlMessage);
        }
    }

    public int getParticipantCount(String s) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return getInteraction(s).getLifeLineNum();
    }

    public UmlLifeline getParticipantCreator(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return getInteraction(s).getParticipantCreator(s1);
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return getInteraction(s).getParticipantLostAndFound(s1);
    }

    private MyInteraction getInteraction(String name) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        ArrayList<MyInteraction> interactions = myInteractions.getOrDefault(name, null);
        if (interactions == null) {
            throw new InteractionNotFoundException(name);
        } else if (interactions.size() > 1) {
            throw new InteractionDuplicatedException(name);
        }
        return interactions.get(0);
    }

    private void checkAttribute(UmlLifeline umlLifeline) {
        String id1 = ((UmlAttribute) myElements.get(umlLifeline.getRepresent())).getParentId();
        String id2 = ((MyInteraction) myElements.get(umlLifeline.getParentId()))
                .getUmlInteraction().getParentId();
        if (!Objects.equals(id1, id2)) {
            checkRule006 = false;
        }

    }

    public void checkForUml006() throws UmlRule006Exception {
        if (!checkRule006) {
            throw new UmlRule006Exception();
        }
    }

    public void checkForUml007() throws UmlRule007Exception {
        for (MyInteraction item : myInteractionsForCheck) {
            if (!item.isCheckRule007()) {
                throw new UmlRule007Exception();
            }
        }
    }
}
