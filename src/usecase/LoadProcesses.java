package usecase;

import domain.BankerObj;
import domain.ProcessObj;
import port.Cli;
import port.FileDataInput;

public class LoadProcesses {
    public static void loadInstance(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
        for (int process = 0; process < bankerObj.getProcesses(); process++) {
            String[] mtxLineArr = fileDataInput.getLine().split(" ");
            bankerObj.addMtxClaimC(new ProcessObj(bankerObj.getIdCounter(), new int[bankerObj.getResources()]));
            bankerObj.incIdCounter();
            for (int resource = 0; resource < bankerObj.getResources(); resource++) {
                bankerObj.getMtxClaimC().get(process).setResource(resource, Integer.parseInt(mtxLineArr[resource]));
            }
        }

        for (int process = 0; process < bankerObj.getProcesses(); process++) {
            String[] mtxLineArr = fileDataInput.getLine().split(" ");
            bankerObj.addMtxAllocationA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
            bankerObj.addMtxNeedCA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
            bankerObj.incIdCounter1();

            for (int resource = 0; resource < bankerObj.getResources(); resource++) {
                bankerObj.getMtxAllocationA().get(process).setResource(resource, Integer.parseInt(mtxLineArr[resource]));
                bankerObj.getMtxNeedCA().get(process).setResource(resource,bankerObj.getMtxClaimC().get(process).getResource(resource) -
                        bankerObj.getMtxAllocationA().get(process).getResource(resource));
                bankerObj.incAvailableResources(resource, bankerObj.getMtxAllocationA().get(process).getResource(resource));
            }
        }

        for (int resource = 0; resource < bankerObj.getResources(); resource++)
            bankerObj.setAvailableResource(resource,
                    bankerObj.getMaxResource(resource) - bankerObj.getAvailableResource(resource));

        cli.printMessage("\n\nAvailable resources: \n");
        cli.printVector("R", bankerObj.getAvailableResources());
        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(),
                bankerObj.getResources(), bankerObj.getProcesses(), -1);

    }
}
