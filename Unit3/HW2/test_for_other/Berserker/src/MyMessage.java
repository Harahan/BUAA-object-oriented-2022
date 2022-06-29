import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;       // sender
    private final Person person2;       // receiver
    private final Group group;          // receiver
    
    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.type = 0;
        this.group = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
    }
    
    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.group = messageGroup;
    }
   
    @Override
    public int getType() {
        return this.type;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public int getSocialValue() {
        return this.socialValue;
    }
    
    @Override
    public Person getPerson1() {
        return this.person1;
    }
    
    @Override
    public Person getPerson2() {
        return this.person2;
    }
    
    @Override
    public Group getGroup() {
        return this.group;
    }
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Message)) {
            return false;
        }
        return ((Message)obj).getId() == id;
    }
}
