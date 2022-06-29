import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;
    private MyDisjointSetUnion dsu;
    private ArrayList<MyValue> values;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        dsu = new MyDisjointSetUnion();
        values = new ArrayList<>();
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (people.containsKey(id)) {
            return people.get(id);
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), person);
        dsu.addPerson(person.getId());
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        if (id1 == id2 && contains(id1)) {
            return;
        }
        MyPerson mp1 = (MyPerson) getPerson(id1);
        MyPerson mp2 = (MyPerson) getPerson(id2);
        mp1.addAcquaintance(getPerson(id2), value);
        mp2.addAcquaintance(getPerson(id1), value);
        dsu.merge(id1, id2);
        MyValue tmp = new MyValue(id1, id2, value);
        values.add(tmp);
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (id1 == id2) {
            return true;
        }
        return dsu.isConnected(id1, id2);
    }

    @Override
    public int queryBlockSum() {
        return dsu.getSum();
    }

    @Override
    public int queryLeastConnection(int id) throws MyPersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        dsu.addValues(values);
        return dsu.kruskal(id);
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        if (((MyGroup) group).getPeople().size() < 1111) {
            group.addPerson(person);
        }
    }

    @Override
    public int queryGroupPeopleSum(int id) throws MyGroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return ((MyGroup) groups.get(id)).getPeopleSum();
    }

    @Override
    public int queryGroupValueSum(int id) throws MyGroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        int sum = 0;
        HashMap<Integer, Person> groupPeople = ((MyGroup) groups.get(id)).getPeople();
        for (MyValue value : values) {
            if (groupPeople.containsKey(value.getId1()) &&
                    groupPeople.containsKey(value.getId2())) {
                sum += value.getValue();
            }
        }
        return sum * 2;
    }

    @Override
    public int queryGroupAgeVar(int id) throws MyGroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        group.delPerson(person);
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws
            MyEqualMessageIdException, MyEqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws MyRelationNotFoundException,
            MyMessageIdNotFoundException, MyPersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = messages.get(id);
        if (message.getType() == 0 &&
                !message.getPerson1().isLinked(message.getPerson2())) {
            throw new MyRelationNotFoundException(
                    message.getPerson1().getId(), message.getPerson2().getId());
        }
        if (message.getType() == 1 &&
                !message.getGroup().hasPerson(message.getPerson1())) {
            throw new MyPersonIdNotFoundException(message.getPerson1().getId());
        }

        int socialValue = message.getSocialValue();
        if (message.getType() == 0) {
            message.getPerson1().addSocialValue(socialValue);
            message.getPerson2().addSocialValue(socialValue);
            messages.remove(id);
            ((MyPerson) message.getPerson2()).addMessage(message);
        } else if (message.getType() == 1) {
            for (Person tmp : ((MyGroup) message.getGroup()).getPeople().values()) {
                tmp.addSocialValue(socialValue);
            }
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws MyPersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws MyPersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }
}
