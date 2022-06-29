package elevator;

import com.oocourse.TimableOutput;

public class MyOutPut {
    public static synchronized long myPrintln(String str) {
        return TimableOutput.println(str);
    }
}
