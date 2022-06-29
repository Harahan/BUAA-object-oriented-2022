import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.ElementType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import classdiagram.ClassDiagram;
import sequencediagram.SequenceDiagram;
import statediagram.StateDiagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final ClassDiagram classDiagram;
    private final StateDiagram stateDiagram;
    private final SequenceDiagram sequenceDiagram;

    public MyImplementation(UmlElement... elements) {
        HashMap<String, Object> myElements = new HashMap<>();
        // id, myElem --- myClass, myInterface, myOperation, umlAttribute
        // myRegion, myStateMachine, myState, umlTransition,
        // myInteraction, myLifeLine, umlEndpoint.
        classDiagram = new ClassDiagram(myElements);
        stateDiagram = new StateDiagram(myElements);
        sequenceDiagram = new SequenceDiagram(myElements);
        parse1(elements);
        parse2(elements);
        parse3(elements);
        parse4(elements);
        parse5(elements);
    }

    private void parse1(UmlElement... elements) {
        for (UmlElement element : elements) {
            ElementType elementType = element.getElementType();
            switch (elementType) {
                case UML_CLASS:
                    classDiagram.parseUmlClass((UmlClass) element);
                    break;
                case UML_INTERFACE:
                    classDiagram.parseUmlInterface((UmlInterface) element);
                    break;
                case UML_STATE_MACHINE:
                    stateDiagram.parseUmlStateMachine((UmlStateMachine) element);
                    break;
                case UML_INTERACTION:
                    sequenceDiagram.parseUmlInteraction((UmlInteraction) element);
                    break;
                case UML_ASSOCIATION:
                    classDiagram.parseUmlAssociation((UmlAssociation) element);
                    break;
                default:
                    break;
            }
        }
    }

    private void parse2(UmlElement... elements) {
        for (UmlElement element : elements) {
            ElementType elementType = element.getElementType();
            switch (elementType) {
                case UML_ATTRIBUTE:
                    classDiagram.parseUmlAttribute((UmlAttribute) element);
                    break;
                case UML_OPERATION:
                    classDiagram.parseUmlOperation((UmlOperation) element);
                    break;
                case UML_REGION:
                    stateDiagram.parseUmlRegion((UmlRegion) element);
                    break;
                case UML_ENDPOINT:
                    sequenceDiagram.parseUmlEndpoint((UmlEndpoint) element);
                    break;
                case UML_ASSOCIATION_END:
                    classDiagram.parseUmlAssociationEnd((UmlAssociationEnd) element);
                    break;
                default:
                    break;
            }
        }
    }

    private void parse3(UmlElement... elements) {
        for (UmlElement element : elements) {
            ElementType elementType = element.getElementType();
            switch (elementType) {
                case UML_PARAMETER:
                    classDiagram.parseUmlParameter((UmlParameter) element);
                    break;
                case UML_GENERALIZATION:
                    classDiagram.parseUmlGeneration((UmlGeneralization) element);
                    break;
                case UML_INTERFACE_REALIZATION:
                    classDiagram.parseUmlInterfaceRealization((UmlInterfaceRealization) element);
                    break;
                case UML_STATE:
                    stateDiagram.parseUmlState((UmlState) element);
                    break;
                case UML_FINAL_STATE:
                    stateDiagram.parseUmlFinalState((UmlFinalState) element);
                    break;
                case UML_PSEUDOSTATE:
                    stateDiagram.parseUmlPseudoState((UmlPseudostate) element);
                    break;
                case UML_LIFELINE:
                    sequenceDiagram.parseUmlIifeLine((UmlLifeline) element); // after umlAttribute
                    break;
                default:
                    break;
            }
        }
    }

    public void parse4(UmlElement... elements) {
        for (UmlElement element : elements) {
            ElementType elementType = element.getElementType();
            switch (elementType) {
                case UML_TRANSITION:
                    stateDiagram.parseUmlTransition((UmlTransition) element);
                    break;
                case UML_MESSAGE:
                    sequenceDiagram.parseUmlMessage((UmlMessage) element);
                    break;
                default:
                    break;
            }
        }
    }

    public void parse5(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_EVENT) {
                stateDiagram.parseUmlEvent((UmlEvent) element);
            }
        }
    }

    @Override
    public int getClassCount() {
        return classDiagram.getClassCount();
    }

    @Override
    public int getClassSubClassCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassSubClassCount(s);
    }

    @Override
    public int getClassOperationCount(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationCount(s);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassOperationVisibility(s, s1);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(String s, String s1) throws
            ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return classDiagram.getClassOperationCouplingDegree(s, s1);
    }

    @Override
    public int getClassAttributeCouplingDegree(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassAttributeCouplingDegree(s);
    }

    @Override
    public List<String> getClassImplementInterfaceList(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassImplementInterfaceList(s);
    }

    @Override
    public int getClassDepthOfInheritance(String s) throws
            ClassNotFoundException, ClassDuplicatedException {
        return classDiagram.getClassDepthOfInheritance(s);
    }

    @Override
    public int getParticipantCount(String s) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        return sequenceDiagram.getParticipantCount(s);
    }

    @Override
    public UmlLifeline getParticipantCreator(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return sequenceDiagram.getParticipantCreator(s, s1);
    }

    @Override
    public Pair<Integer, Integer> getParticipantLostAndFound(String s, String s1) throws
            InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return sequenceDiagram.getParticipantLostAndFound(s, s1);
    }

    @Override
    public int getStateCount(String s) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateDiagram.getStateCount(s);
    }

    @Override
    public boolean getStateIsCriticalPoint(String s, String s1) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return stateDiagram.getStateIsCriticalPoint(s, s1);
    }

    @Override
    public List<String> getTransitionTrigger(String s, String s1, String s2) throws
            StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return stateDiagram.getTransitionTrigger(s, s1, s2);
    }

    @Override
    public void checkForUml001() throws UmlRule001Exception {
        classDiagram.checkForUml001();
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        classDiagram.checkForUml002();
    }

    @Override
    public void checkForUml003() throws UmlRule003Exception {
        classDiagram.checkForUml003();
    }

    @Override
    public void checkForUml004() throws UmlRule004Exception {
        classDiagram.checkForUml004();
    }

    @Override
    public void checkForUml005() throws UmlRule005Exception {
        classDiagram.checkForUml005();
    }

    @Override
    public void checkForUml006() throws UmlRule006Exception {
        sequenceDiagram.checkForUml006();
    }

    @Override
    public void checkForUml007() throws UmlRule007Exception {
        sequenceDiagram.checkForUml007();
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        stateDiagram.checkForUml008();
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        stateDiagram.checkForUml009();
    }
}
