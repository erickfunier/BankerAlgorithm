package thread;

import domain.BankerObj;
import port.Cli;

import static usecase.CheckSafeState.checkSafeState;
import static usecase.ExecuteBanker.executeBanker;

public class ThreadRunBanker extends Thread {
    private final Cli cli;
    private final BankerObj bankerObj;
    private boolean safeState;
    private final ThreadNewProcess threadNewProcess;
    public ThreadRunBanker(Cli cli, BankerObj bankerObj, ThreadNewProcess threadNewProcess) {
        this.cli = cli;
        this.bankerObj = bankerObj;
        this.safeState = true;
        this.threadNewProcess = threadNewProcess;
    }
    @Override
    public void run() {
        safeState = true;

        cli.printMessage("\nBANKER started\n");
        while(safeState) {

            safeState = checkSafeState(cli, bankerObj);
            if (safeState) {
                try {
                    executeBanker(cli, bankerObj);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!safeState && !threadNewProcess.isAlive()) {
                cli.printMessage("\nAvailable resources: \n");
                cli.printVector("R", bankerObj.getAvailableResources());
                cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                        bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), -1);
                cli.printMessage("\nBANKER finished\n");
                return;
            } else if (bankerObj.getProcesses() == 0) {
                cli.printMessage("\nBANKER finished\n");
                return;
            } else {
                cli.printMessage("\nAvailable resources: \n");
                cli.printVector("R", bankerObj.getAvailableResources());
                cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                        bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), -1);
                cli.printMessage("\nBANKER waiting\n");
                safeState = true;
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cli.printMessage("\nBANKER restarting\n");
        }
    }
}
