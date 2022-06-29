import com.oocourse.TimableOutput;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] argv) {
        TimableOutput.initStartTimestamp();  // 初始化时间戳
        ArrayList<Elevator> elevators = new ArrayList<>();
        Scheduler scheduler = new Scheduler();
        Output output = new Output();
        int elevatorNum = 1;
        int num = 5;
        Elevator elevatorA = new Elevator('A', elevatorNum, output, scheduler);
        elevatorA.start();
        Elevator elevatorB = new Elevator('B', elevatorNum, output, scheduler);
        elevatorB.start();
        Elevator elevatorC = new Elevator('C', elevatorNum, output, scheduler);
        elevatorC.start();
        Elevator elevatorD = new Elevator('D', elevatorNum, output, scheduler);
        elevatorD.start();
        Elevator elevatorE = new Elevator('E', elevatorNum, output, scheduler);
        elevatorE.start();
        // InputThread
        InputThread inputThread = new InputThread(scheduler);
        inputThread.start();
    }
}
