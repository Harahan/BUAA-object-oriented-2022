package mymessage;

import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Group;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    private final String string;

    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        string = noticeString;
    }

    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
        string = noticeString;
    }

    @Override
    public String getString() {
        return string;
    }
}
