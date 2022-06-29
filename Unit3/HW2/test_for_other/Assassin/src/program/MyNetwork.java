package program;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import exception.MyEqualGroupIdException;
import exception.MyEqualMessageIdException;
import exception.MyEqualPersonIdException;
import exception.MyEqualRelationException;
import exception.MyGroupIdNotFoundException;
import exception.MyMessageIdNotFoundException;
import exception.MyPersonIdNotFoundException;
import exception.MyRelationNotFoundException;
import program.network.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people = new HashMap<>();
    private HashMap<Integer, Group> groups = new HashMap<>();
    private int peopleSum = 0;
    private int blockSum = 0;
    private int blockId = 0;
    private HashMap<Integer, Integer> fathers = new HashMap<>();
    private HashMap<Integer, Message> messages = new HashMap<>();
    private HashMap<Person, HashSet<Group>> belonged = new HashMap<>();
    private Graph graph = new Graph();

    private int find(int personId) {
        if (personId == fathers.get(personId)) {
            return personId;
        }
        List<Integer> personToUpdate = new ArrayList<>();
        int person = personId;

        while (!fathers.get(person).equals(person)) {
            personToUpdate.add(person);
            person = fathers.get(person);
        }

        int father = person;
        personToUpdate.forEach(id -> fathers.put(id, father));
        return father;
    }

    private void merge(int personId1, int personId2) {
        int f1 = find(personId1);
        int f2 = find(personId2);
        if (f1 == f2) {
            return;
        }
        blockSum--;
        fathers.put(f1, f2);
        fathers.put(personId1, f2);
    }

    @Override
    public boolean contains(int id) {
        return people.get(id) != null;
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), person);
        fathers.put(person.getId(), person.getId());
        peopleSum++;
        blockSum++;
        belonged.put(person, new HashSet<>());
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualRelationException {
        MyPerson p1 = (MyPerson) getPerson(id1);
        MyPerson p2 = (MyPerson) getPerson(id2);
        if (contains(id1) && contains(id2) && !p1.isLinked(p2)) {
            p1.constructLink(p2, value);
            p2.constructLink(p1, value);
            merge(p1.getId(), p2.getId());
            HashSet<Group> p1Belonged = belonged.get(p1);
            HashSet<Group> p2Belonged = belonged.get(p2);
            for (Group p1Group: p1Belonged) {
                if (p2Belonged.contains(p1Group)) {
                    ((MyGroup) p1Group).addValue(value);
                }
            }
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws PersonIdNotFoundException,
            RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            throw new MyRelationNotFoundException(id1, id2);
        }
    }

    @Override
    public int queryPeopleSum() {
        return peopleSum;
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        int f1 = find(id1);
        int f2 = find(id2);
        return Objects.equals(f1, f2);
    }

    @Override
    public int queryBlockSum() {
        return blockSum;
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        Person target = getPerson(id);
        int fatherId = find(id);
        HashSet<Person> brother = people.values().stream().
                filter(p -> fatherId == find(p.getId())).
                collect(Collectors.toCollection(HashSet::new));
        return graph.prim(target, brother);
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.get(group.getId()) == null) {
            groups.put(group.getId(), group);
        } else {
            throw new MyEqualGroupIdException(group.getId());
        }
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group != null && contains(id1) && !group.hasPerson(person)) {
            int numberOfGroup = group.getSize();
            if (numberOfGroup < 1111) {
                group.addPerson(person);
                belonged.get(person).add(group);
            }
        } else if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyEqualPersonIdException(id1);
        }
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group != null && contains(id1) && group.hasPerson(person)) {
            belonged.get(person).remove(group);
            group.delPerson(person);
        } else if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyEqualPersonIdException(id1);
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.get(id) != null;
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = getMessage(id);
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (message.getType() == 0 &&
                !message.getPerson1().isLinked(message.getPerson2())) {
            throw new MyRelationNotFoundException(message.getPerson1().getId(),
                    message.getPerson2().getId());
        } else if (message.getType() == 1 &&
                !(message.getGroup().hasPerson(message.getPerson1()))) {
            throw new MyPersonIdNotFoundException(message.getPerson1().getId());
        }
        MyPerson deliver = (MyPerson) message.getPerson1();
        messages.remove(message.getId());
        if (message.getType() == 0 && !deliver.equals(message.getPerson2())) {
            MyPerson receiver = (MyPerson) message.getPerson2();
            deliver.addSocialValue(message.getSocialValue());
            receiver.receiveMessage(message);
        } else if (message.getType() == 1) {
            MyGroup group = (MyGroup) message.getGroup();
            group.receiveMessage(message);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }
}