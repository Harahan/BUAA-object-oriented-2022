package sequencediagram;

import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyInteraction {
    private final HashMap<String, ArrayList<MyLifeLine>> totLifelines = new HashMap<>();

    private final HashSet<MyLifeLine> totLifeLinesForCheck = new HashSet<>();
    private final String name;
    private int lifeLineNum = 0;

    private final UmlInteraction umlInteraction;

    public MyInteraction(UmlInteraction umlInteraction) {
        name = umlInteraction.getName();
        this.umlInteraction = umlInteraction;
    }

    public UmlInteraction getUmlInteraction() {
        return umlInteraction;
    }

    public void addLifLine(MyLifeLine myLifeLine) {
        ++lifeLineNum;
        totLifeLinesForCheck.add(myLifeLine);
        myLifeLine.setInteractionName(name);
        totLifelines.merge(myLifeLine.getName(), new ArrayList<MyLifeLine>() {{
                add(myLifeLine);
            }}, (oldList, newList) -> {
                oldList.add(myLifeLine);
                return oldList;
            });
    }

    public int getLifeLineNum() {
        return lifeLineNum;
    }

    public UmlLifeline getParticipantCreator(String s1) throws
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return getLifeLine(s1).getCreator();
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(String s1) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        return getLifeLine(s1).getLostAndFound();
    }

    private MyLifeLine getLifeLine(String name) throws
            LifelineNotFoundException, LifelineDuplicatedException {
        ArrayList<MyLifeLine> myLifeLines = totLifelines.getOrDefault(name, null);
        if (myLifeLines == null) {
            throw new LifelineNotFoundException(this.name, name);
        } else if (myLifeLines.size() > 1) {
            throw new LifelineDuplicatedException(this.name, name);
        }
        return myLifeLines.get(0);
    }

    public boolean isCheckRule007() {
        for (MyLifeLine item : totLifeLinesForCheck) {
            if (!item.isCheckRule007()) {
                return false;
            }
        }
        return true;
    }
}
