package mycode;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people = new HashMap<>();
    private HashMap<Integer, Group> groups = new HashMap<>();
    private HashMap<Integer, Message> messages = new HashMap<>();
    private HashSet<Edge> edges = new HashSet<>();
    private MyDsu<Person> dsu = new MyDsu<>();
    private int blockSum = 0;

    public MyNetwork() {
    }

    @Override
    public boolean contains(int id) { //查询人在不在
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) { //按id取人 找不到返回null
        return people.getOrDefault(id, null);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException { // 加人,personId重复则报错
        if (people.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            dsu.addElement(person);
            blockSum++;
        }

    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        // 建立图中两节点id1, id2的联系(要改person,相当于一个邻接表)， 并给予边权 value
        // 异常: 1.id找不到人, 2.已经有link了（也就是说没有重边）
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        // 人有了
        MyPerson person1 = (MyPerson) getPerson(id1);
        MyPerson person2 = (MyPerson) getPerson(id2);
        if (dsu.findRoot(person1) != dsu.findRoot(person2)) {
            blockSum--;
        }
        person1.addLink(person2, value);
        person2.addLink(person1, value);

        edges.add(new Edge(person1, person2, value));

        dsu.unionSet(person1, person2);
        for (Map.Entry<Integer, Group> groupEntry : groups.entrySet()) {
            Group group = groupEntry.getValue();
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup) group).addValueSum(value);
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        // 查两个人的value
        // 异常: 1. id查不到人， 2. 查到人了, 俩人妹有链接
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }

        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public int queryPeopleSum() {
        // 总人数
        return people.size();
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        // 查询id1, id2 间能否通过中间人连上， 所谓array就是路径
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return dsu.findRoot(getPerson(id1)) == dsu.findRoot(getPerson(id2));
    }

    @Override
    public int queryBlockSum() {
        // 查询联通分量的个数
        return blockSum;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        // 添加组
        // 异常: 1.重复组id
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }

        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        // 查询组 查不到返回null
        return groups.getOrDefault(id, null);
    }

    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        HashMap<Integer, Person> peopleInBlock = new HashMap<>();
        ArrayList<Edge> edgesInBlock = new ArrayList<>();
        int ans = 0;
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        Person root = dsu.findRoot(getPerson(id));
        for (Map.Entry<Integer, Person> personEntry : people.entrySet()) {
            Person rootp = dsu.findRoot(personEntry.getValue());
            if (root.equals(rootp)) {
                peopleInBlock.put(personEntry.getKey(), personEntry.getValue());
            }
        } // V
        for (Edge edge : edges) {
            Person person1 = edge.getPerson1();
            Person person2 = edge.getPerson2();
            if (peopleInBlock.containsKey(person1.getId())
                    && peopleInBlock.containsKey(person2.getId())) {
                edgesInBlock.add(edge);
            }
        } // E

        MyDsu<Person> dsu4kruskal = new MyDsu<>();
        for (Map.Entry<Integer, Person> personEntry : people.entrySet()) {
            dsu4kruskal.addElement(personEntry.getValue());
        }
        Collections.sort(edgesInBlock);
        int cnt = 0;
        for (Edge edge : edgesInBlock) {
            Person person1 = edge.getPerson1();
            Person person2 = edge.getPerson2();
            Person root1 = dsu4kruskal.findRoot(person1);
            Person root2 = dsu4kruskal.findRoot(person2);
            if (root1 == root2) {
                continue;
            }
            cnt++;
            dsu4kruskal.unionSet(root1, root2);
            ans += edge.getVal();
            if (cnt >= peopleInBlock.size() - 1) {
                break;
            }
        }
        return ans;
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return groups.get(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.getOrDefault(id, null);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (containsMessage(id) && getMessage(id).getType() == 0
                && !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(
                    getMessage(id).getPerson1().getId(), getMessage(id).getPerson2().getId());
        } else if (containsMessage(id) && getMessage(id).getType() == 1
                && !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        }
        Message message = getMessage(id);

        if (message.getType() == 0) {
            Person person1 = message.getPerson1();
            Person person2 = message.getPerson2();
            person1.addSocialValue(message.getSocialValue());
            person2.addSocialValue(message.getSocialValue());

            messages.remove(id);

            ((MyPerson) person2).addFirst(message);

        } else if (message.getType() == 1) {
            Group group = message.getGroup();
            ((MyGroup) group).addEverySocialValue(message.getSocialValue());
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }

    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        // 把personId为id1的人 加入到 groupId为id2的组 中
        // 异常: 1.找不到组号， 2.找不到人号 3. 组中已经有id一样的人了
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }

        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (!(group.getSize() >= 1111)) {
            group.addPerson(person);
        }

    }

    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        // 把personId为id1的人 从 groupId为id2的组 中删除
        // 异常: 1.找不到组号， 2.找不到人号 3. 组中妹有人
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        group.delPerson(person);
    }
}
