package mymessage;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

public class MyMessage implements Message {
    private final int id;
    private final Person person1;
    private final Person person2;
    private final Group group;
    private final int type;
    private final int socialValue;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        type = 0;
        group = null;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        person2 = messagePerson2;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        type = 1;
        person2 = null;
        id = messageId;
        socialValue = messageSocialValue;
        person1 = messagePerson1;
        group = messageGroup;
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
    public boolean equals(Object obj) {
        return obj instanceof Message && ((Message) obj).getId() == id;
    }
}
