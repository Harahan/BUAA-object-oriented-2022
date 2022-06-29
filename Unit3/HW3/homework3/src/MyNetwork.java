import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Network;
import myexceptions.MyEqualGroupIdException;
import myexceptions.MyEqualRelationException;
import myexceptions.MyRelationNotFoundException;
import myexceptions.MyPersonIdNotFoundException;
import myexceptions.MyEqualPersonIdException;
import myexceptions.MyGroupIdNotFoundException;
import myexceptions.MyMessageIdNotFoundException;
import myexceptions.MyEqualMessageIdException;
import myexceptions.MyEqualEmojiIdException;
import myexceptions.MyEmojiIdNotFoundException;
import util.DisjointSets;
import util.Edge;
import util.Node;

import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.Set;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();
    private final DisjointSets network = new DisjointSets();
    private final HashMap<Integer, Message> messages = new HashMap<>();
    private final HashMap<Integer, Integer> cache = new HashMap<>();
    private final HashMap<Integer, Integer> dirty = new HashMap<>();
    private final HashMap<Integer, Integer> emojis = new HashMap<>();

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

    private HashMap<Integer, Integer> getEdges(int id) {
        return ((MyPerson) getPerson(id)).getAcquaintance();
    }

    private PriorityQueue<Edge> getEdges(PriorityQueue<Edge> edges, Person nxt, Person pre) {
        int uid = nxt.getId();
        if (dsu.contains(uid)) {
            return edges;
        }
        dsu.add(uid);
        Set<Map.Entry<Integer, Integer>> table = ((MyPerson)nxt).getAcquaintance().entrySet();
        for (Map.Entry<Integer, Integer> entry : table) {
            int vid = entry.getKey();
            if (pre == null || pre.getId() != vid) {
                edges.add(new Edge(uid, vid, entry.getValue()));
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
    public void addMessage(Message message) throws EqualMessageIdException,
            EmojiIdNotFoundException, EqualPersonIdException {
        if (!containsMessage(message.getId()) && message.getPerson1() != message.getPerson2() &&
                (!(message instanceof EmojiMessage) ||
                        containsEmojiId(((EmojiMessage) message).getEmojiId()))) {
            messages.put(message.getId(), message); // type = 1, p1 != p2
        } else if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
    }

    @Override
    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }

    public void forSendMessage0(Person person1, Person person2, Message message, int id) {
        person1.addSocialValue(message.getSocialValue());
        person2.addSocialValue(message.getSocialValue());
        if (message instanceof RedEnvelopeMessage) {
            int money = ((RedEnvelopeMessage) message).getMoney();
            person1.addMoney(-money);
            person2.addMoney(money);
        } else if (message instanceof EmojiMessage) {
            emojis.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
        }
        messages.remove(id);
        ((MyPerson) person2).addMessage(message);
    }

    public void forSendMessage1(Person person1, Group group, Message message, int id) {
        ((MyGroup) group).addSocialValue(message.getSocialValue());
        if (message instanceof RedEnvelopeMessage) {
            int money = ((RedEnvelopeMessage) message).getMoney() / group.getSize();
            person1.addMoney(-money * group.getSize());
            ((MyGroup) group).addMoney(money);
        } else if (message instanceof EmojiMessage) {
            emojis.merge(((EmojiMessage) message).getEmojiId(), 1, Integer::sum);
        }
        messages.remove(id);
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
                    forSendMessage0(person1, person2, message, id);
                } else {
                    throw new MyRelationNotFoundException(person1.getId(), person2.getId());
                }
            } else {
                Group group = message.getGroup();
                if (group.hasPerson(person1)) {
                    forSendMessage1(person1, group, message, id);
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

    @Override
    public boolean containsEmojiId(int id) {
        return emojis.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (!containsEmojiId(id)) {
            emojis.put(id, 0);
        } else {
            throw new MyEqualEmojiIdException(id);
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getMoney();
        }
        throw new MyPersonIdNotFoundException(id);
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (containsEmojiId(id)) {
            return emojis.get(id);
        }
        throw new MyEmojiIdNotFoundException(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        emojis.entrySet().removeIf(entry -> entry.getValue() < limit);
        messages.entrySet().removeIf(entry -> entry.getValue() instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) entry.getValue()).getEmojiId()));
        return emojis.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (contains(personId)) {
            ((MyPerson) getPerson(personId)).clearNotices();
        } else {
            throw new MyPersonIdNotFoundException(personId);
        }
    }

    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (containsMessage(id) && getMessage(id).getType() == 0) {
            Message message = getMessage(id);
            Person person1 = message.getPerson1();
            Person person2 = message.getPerson2();
            int id1 = person1.getId();
            int id2 = person2.getId();
            try {
                if (!isCircle(id1, id2)) {
                    return -1;
                } else {
                    forSendMessage0(person1, person2, message, id);
                    return getMinPath(id1, id2);
                }
            } catch (PersonIdNotFoundException e) {
                System.out.println("Data Error!!!");
            }
        }
        throw new MyMessageIdNotFoundException(id);
    }

    private int getMinPath(int u, int v) {
        PriorityQueue<Node> q = new PriorityQueue<>(Comparator.comparingInt(Node::getDis));
        HashMap<Integer, Integer> dis = new HashMap<>();
        HashSet<Integer> in = new HashSet<>();
        dis.put(u, 0);
        q.offer(new Node(u, 0));
        while (!q.isEmpty()) {
            int s = q.poll().getId();
            if (!in.contains(s) && s != v) {
                in.add(s);
                HashMap<Integer, Integer> es = getEdges(s);
                int sw = dis.get(s);
                es.forEach((e, w) -> {
                    int ew = sw + w;
                    if (dis.getOrDefault(e, Integer.MAX_VALUE) > ew) {
                        dis.put(e, ew);
                        q.offer(new Node(e, ew));
                    }
                });
            } else if (s == v) {
                break;
            }
        }
        return dis.getOrDefault(v, Integer.MAX_VALUE);
    }
}
