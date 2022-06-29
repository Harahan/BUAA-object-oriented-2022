import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import myexceptions.MyEqualGroupIdException;
import myexceptions.MyEqualRelationException;
import myexceptions.MyRelationNotFoundException;
import myexceptions.MyPersonIdNotFoundException;
import myexceptions.MyEqualPersonIdException;
import myexceptions.MyGroupIdNotFoundException;
import myexceptions.MyMessageIdNotFoundException;
import myexceptions.MyEqualMessageIdException;
import util.DisjointSets;
import util.Edge;

import java.util.HashMap;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Comparator;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();
    private final DisjointSets network = new DisjointSets();
    private final HashMap<Integer, Message> messages = new HashMap<>();
    private final HashMap<Integer, Integer> cache = new HashMap<>();
    private final HashMap<Integer, Integer> dirty = new HashMap<>();

    private DisjointSets dsu;

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
        int id = person.getId();
        if (!contains(id)) {
            people.put(id, person);
            network.add(id);
            cache.put(id, 0);
            dirty.put(id, 1);
        } else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    private void check(int id1, int id2, int value) {
        int fa1 = network.find(id1);
        int fa2 = network.find(id2);
        if (dirty.get(fa1) == 0 && dirty.get(fa2) == 0 && fa1 != fa2) {
            cache.merge(fa1, cache.get(fa2) + value, Integer::sum);
            cache.put(fa2, cache.get(fa1));
        } else {
            dirty.put(fa1, 1);
            dirty.put(fa2, 1);
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            ((MyPerson) getPerson(id1)).addAcquaintance(id2, value);
            ((MyPerson) getPerson(id2)).addAcquaintance(id1, value);
            check(id1, id2, value);
            network.merge(id1, id2);
            groups.forEach((id, group) -> {
                if (group.hasPerson(getPerson(id1)) && group.hasPerson(getPerson(id2))) {
                    ((MyGroup) group).addRelation(value);
                }
            });
        } else if (!contains(id1) || !contains(id2)) {
            throw new MyPersonIdNotFoundException((!contains(id1)) ? id1 : id2);
        } else {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else if (!contains(id1) || !contains(id2)) {
            throw new MyPersonIdNotFoundException((!contains(id1)) ? id1 : id2);
        }
        throw new MyRelationNotFoundException(id1, id2);
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            return network.find(id1) == network.find(id2);
        }
        throw new MyPersonIdNotFoundException((!contains(id1)) ? id1 : id2);
    }

    @Override
    public int queryBlockSum() {
        return network.getTot();
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            int fa = network.find(id);
            if (dirty.get(fa) == 1) {
                dirty.put(fa, 0);
                dsu = new DisjointSets();
                cache.put(fa, getMinSpanningTree(
                        getEdges(new PriorityQueue<>(Comparator.comparingInt(Edge::getWx)),
                                people.get(fa), null)));
            }
            return cache.get(fa);
        }
        throw new MyPersonIdNotFoundException(id);
    }

    private int getMinSpanningTree(PriorityQueue<Edge> edges) {
        int n = dsu.getSize() - 1;
        int min = 0;
        while (n > 0 && !edges.isEmpty()) {
            Edge edge = edges.poll();
            int u = edge.getUx();
            int v = edge.getVx();
            if (dsu.find(u) != dsu.find(v)) {
                n -= 1;
                dsu.merge(u, v);
                min += edge.getWx();
            }
        }
        return min;
    }

    private PriorityQueue<Edge> getEdges(PriorityQueue<Edge> edges, Person nxt, Person pre) {
        int uid = nxt.getId();
        if (dsu.contains(uid)) {
            return edges;
        }
        dsu.add(uid);
        Set<Integer> table = ((MyPerson)nxt).getAcquaintance().keySet();
        for (Integer vid : table) {
            if (pre == null || pre.getId() != vid) {
                edges.add(new Edge(uid, vid, nxt.queryValue(people.get(vid))));
                getEdges(edges, people.get(vid), nxt);
            }
        }
        return edges;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (!groups.containsKey(group.getId())) {
            groups.put(group.getId(), group);
        } else {
            throw new MyEqualGroupIdException(group.getId());
        }
    }

    @Override
    public Group getGroup(int id) {
        return groups.getOrDefault(id, null);
    }

    @Override
    public void addToGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (groups.containsKey(id2) && contains(id1)
                && !getGroup(id2).hasPerson(getPerson(id1)) && getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (groups.containsKey(id)) {
            return getGroup(id).getSize();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (groups.containsKey(id)) {
            return getGroup(id).getValueSum();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (groups.containsKey(id)) {
            return getGroup(id).getAgeVar();
        }
        throw new MyGroupIdNotFoundException(id);
    }

    @Override
    public void delFromGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (groups.containsKey(id2) && contains(id1)
                && getGroup(id2).hasPerson(getPerson(id1))) {
            getGroup(id2).delPerson(getPerson(id1));
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyEqualPersonIdException(id1);
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (!containsMessage(message.getId()) && message.getPerson1() != message.getPerson2()) {
            messages.put(message.getId(), message); // type = 1, p1 != p2
        } else if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
    }

    @Override
    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (containsMessage(id)) {
            Message message = getMessage(id);
            Person person1 = message.getPerson1();
            if (message.getType() == 0) { // p1 != p2 is satisfied in add
                Person person2 = message.getPerson2();
                if (person1.isLinked(person2)) {
                    person1.addSocialValue(message.getSocialValue());
                    person2.addSocialValue(message.getSocialValue());
                    messages.remove(id);
                    ((MyPerson) person2).addMessage(message);
                } else {
                    throw new MyRelationNotFoundException(person1.getId(), person2.getId());
                }
            } else {
                Group group = message.getGroup();
                if (group.hasPerson(person1)) {
                    ((MyGroup) group).addSocialValue(message.getSocialValue());
                    messages.remove(id);
                } else {
                    throw new MyPersonIdNotFoundException(person1.getId());
                }
            }
        } else {
            throw new MyMessageIdNotFoundException(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        }
        throw new MyPersonIdNotFoundException(id);
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        }
        throw new MyPersonIdNotFoundException(id);
    }
}
