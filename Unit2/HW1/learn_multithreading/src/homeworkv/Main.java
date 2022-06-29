package homeworkv;

//import com.hansbug.arguments.ResolveCmd;
import homeworkv.data.RequestQueue;
import homeworkv.thread.Elevator;
import homeworkv.thread.RequestInputDevice;

public class Main {
    public static void main(String[] args) {
        //ResolveCmd.initialize(args);

        RequestQueue requestQueue = new RequestQueue();
        new RequestInputDevice(requestQueue).start();
        new Elevator(requestQueue).start();
    }
}
