import com.oocourse.TimableOutput;

public class OutputThread {

    private static OutputThread outputThread = new OutputThread();

    private OutputThread(){}

    public static OutputThread getInstance() {
        return outputThread;
    }

    public synchronized void println(String msg) {
        TimableOutput.println(msg);
    }
}