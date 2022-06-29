import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import myexceptions.MyGroupIdNotFoundException;
import myexceptions.MyEqualGroupIdException;
import myexceptions.MyEqualPersonIdException;
import myexceptions.MyPersonIdNotFoundException;
import myexceptions.MyRelationNotFoundException;
import myexceptions.MyEqualRelationException;
import util.DisjointSets;
import java.util.HashMap;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<>();
    private final HashMap<Integer, Group> groups = new HashMap<>();
    private final DisjointSets network = new DisjointSets();

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
            network.add(person.getId());
        } else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            ((MyPerson) getPerson(id1)).addAcquaintance(id2, value);
            ((MyPerson) getPerson(id2)).addAcquaintance(id1, value);
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
    public boolean isCircle(int id1, int id2)
            throws PersonIdNotFoundException {
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
        if (groups.containsKey(id2) && people.containsKey(id1)
                && !getGroup(id2).hasPerson(getPerson(id1)) && getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    @Override
    public void delFromGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (groups.containsKey(id2) && people.containsKey(id1)
                && getGroup(id2).hasPerson(getPerson(id1))) {
            getGroup(id2).delPerson(getPerson(id1));
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else {
            throw new MyEqualPersonIdException(id1);
        }
    }
}
