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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final HashMap<Integer, Group> groups;
    private int qbsAns = 0;
    private ArrayList<Message> messages;
    private HashMap<Person, Person> fathers = new HashMap<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    
    public MyNetwork() {
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
        this.messages = new ArrayList<>();
    }
    
    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }
    
    @Override
    public Person getPerson(int id) {
        return people.getOrDefault(id, null);
    }
    
    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!contains(person.getId())) {
            people.put(person.getId(), person);
            fathers.put(person, person);
            qbsAns++;
        } else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }
    
    private void addEdge(Edge edge) {
        int l = 0;
        int r = edges.size();
        int mid;
        int value = edge.getValue();
        while (l < r) {
            mid = (l + r) / 2;
            if (value > edges.get(mid).getValue()) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        edges.add(l, edge);
    }
    
    private Person findFather(HashMap<Person, Person> fa, Person person) {
        //if (fa.containsKey(person)) {
        if (fa.get(person) == person) {
            return person;
        } else {
            Person father = findFather(fa, fa.get(person));
            fa.put(person, father);
            return father;
        }
        //}
        //return null;
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
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        //Person father1 = MyPerson.findFather(person1);
        //Person father2 = MyPerson.findFather(person2);
        Person father1 = findFather(fathers, person1);
        Person father2 = findFather(fathers, person2);
        if (father1.getId() != father2.getId()) {
            //((MyPerson)father1).setFather(father2);
            fathers.put(father1, father2);
            qbsAns--;
        }
        addEdge(new Edge(person1, person2, value));
        ((MyPerson) getPerson(id1)).addAcquaintance(getPerson(id2), value);
        ((MyPerson) getPerson(id2)).addAcquaintance(getPerson(id1), value);
        for (Integer gid : groups.keySet()) {
            Group group = getGroup(gid);
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup)group).addValueSum(value);
            }
        }
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
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        //return MyPerson.findFather(person1) == MyPerson.findFather(person2);
        return findFather(fathers, person1).getId() == findFather(fathers, person2).getId();
    }
    
    @Override
    public int queryBlockSum() {
        return qbsAns;
    }
    
    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException { // TODO
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        int ans = 0;
        int totalPoints = 0;
        HashMap<Integer, Person> reachable = new HashMap<>();
        for (Integer pid : this.people.keySet()) {
            if (isCircle(pid, id)) {
                reachable.put(pid, people.get(pid));
                totalPoints++;
            }
        }
        HashMap<Person, Person> fa = new HashMap<>();
        fa.put(person, person);
        for (Edge e : edges) {
            Person person1 = e.getPerson1();
            Person person2 = e.getPerson2();
            if (reachable.containsKey(person1.getId())) {
                if (!fa.containsKey(person1)) {
                    fa.put(person1, person1);
                }
                if (!fa.containsKey(person2)) {
                    fa.put(person2, person2);
                }
                Person father1 = findFather(fa, person1);
                Person father2 = findFather(fa, person2);
                if (father1.getId() != father2.getId()) {
                    totalPoints--;
                    fa.put(father1, father2);
                    ans += e.getValue();
                    if (totalPoints == 1) {
                        break;
                    }
                }
            }
        }
        return ans;
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
        return groups.getOrDefault(id, null);
    }
    
    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        Person person = getPerson(id1);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(person.getId());
        }
        if (group.getSize() < 1111) {
            group.addPerson(person);
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
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        Person person = getPerson(id1);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(person.getId());
        }
        group.delPerson(person);
    }
    
    @Override
    public boolean containsMessage(int id) {
        for (Message message : messages) {
            if (message.getId() == id) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        int msgId = message.getId();
        for (Message m : messages) {
            if (m.getId() == msgId) {
                throw new MyEqualMessageIdException(msgId);
            }
        }
        if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.add(message);
    }
    
    @Override
    public Message getMessage(int id) {
        for (Message m : messages) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }
    
    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException { // TODO
        Message message = getMessage(id);
        if (message == null) {
            throw new MyMessageIdNotFoundException(id);
        }
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        Group group = message.getGroup();
        if (message.getType() == 0 && !person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(person1.getId(), person2.getId());
        }
        if (message.getType() == 1 && !group.hasPerson(person1)) {
            throw new MyPersonIdNotFoundException(person1.getId());
        }
        if (message.getType() == 0 && person1.getId() != person2.getId()) {
            person1.addSocialValue(message.getSocialValue());
            person2.addSocialValue(message.getSocialValue());
            messages.remove(message);
            //person2.getReceivedMessages().add(0, message);
            ((MyPerson)person2).addMessage(message);
        } else if (message.getType() == 1) {
            ((MyGroup)group).receiveMessage(message);
            messages.remove(message);
        }
    }
    
    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getSocialValue();
    }
    
    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getReceivedMessages();
    }
}
