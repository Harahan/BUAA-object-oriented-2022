import com.oocourse.TimableOutput;

public class Output extends TimableOutput {
    public Output() {
        initStartTimestamp();
    }

    public synchronized void println(String s) {
        super.println(s);
        notifyAll();
    }

}
