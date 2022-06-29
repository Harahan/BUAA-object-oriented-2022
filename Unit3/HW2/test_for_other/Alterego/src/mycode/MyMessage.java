package mycode;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.Objects;

public class MyMessage implements Message {

    private int id;
    private int socialValue = 0;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    /*@ ensures type == 0;
      @ ensures group == null;
      @ ensures id == messageId;
      @ ensures socialValue == messageSocialValue;
      @ ensures person1 == messagePerson1;
      @ ensures person2 == messagePerson2;
      @*/
    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        type = 0;
        group = null;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        person2 = messagePerson2;
    } // msg (id, p1, p2, val)

    /*@ ensures type == 1;
      @ ensures person2 == null;
      @ ensures id == messageId;
      @ ensures socialValue == messageSocialValue;
      @ ensures person1 == messagePerson1;
      @ ensures group == messageGroup;
      @*/
    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        type = 1;
        group = messageGroup;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        person2 = null;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        return person2;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Message) {
            return (((Message) o).getId() == id);
        }
        return false;

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}