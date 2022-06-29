import com.oocourse.spec3.main.Runner;
import mymessage.MyEmojiMessage;
import mymessage.MyMessage;
import mymessage.MyNoticeMessage;
import mymessage.MyRedEnvelopeMessage;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class, MyMessage.class,
                MyEmojiMessage.class, MyNoticeMessage.class, MyRedEnvelopeMessage.class);
        runner.run();
    }
}
