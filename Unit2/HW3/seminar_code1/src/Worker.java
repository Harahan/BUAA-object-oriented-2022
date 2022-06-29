public class Worker implements Runnable {
    private final WorkingList workingList; // 这个工人自己的工作列表
    private final int workerId; // 工人ID
    private final WorkingStage workingStage; // 工人所处流水线级

    Worker(int workerId, WorkingList workingList, WorkingStage workingStage) {
        this.workingList = workingList;
        this.workerId = workerId;
        this.workingStage = workingStage;
    }

    private void solveRequest(Request request) { //消耗对应的时间完成工序
        Printer.println("REQUEST: " + request.getRequestCode()
                + ", SOLVE STAGE: " + workingStage + ", WorkerId: " + workerId);
        try {
            Thread.sleep(workingStage.getWorkingTime()); // TODO(3)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Printer.println("REQUEST: " + request.getRequestCode()
                + ", FINISH STAGE: " + workingStage + ", WorkerId: " + workerId);
        request.finishStage(workingStage);
        if (request.allStagesFinished()) { //当前任务完成，输出信息 这个request的所有工序都完成了
            Printer.println("REQUEST FINISH: " + request.getRequestCode());
            RequestCounter.getInstance().release();
        } else { //当前任务没有结束，需要传递给下一个人
            //RequestCounter.getInstance().acquire();
            Controller.getInstance().addRequest(request);
            // TODO(4)
            // 标记当前工序已结束
        }
    }

    @Override
    public void run() {
        while (true) {
            Request request = workingList.getRequest();
            if (request == null) {
                break;
            }
            solveRequest(request);
        }
    }
}
