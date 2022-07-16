package usecase;

import domain.BankerObj;
import domain.ProcessObj;
import port.Cli;
import port.FileDataInput;

public class AddProcess {
    public static void addInstance(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
        String[] mtxLineArr = fileDataInput.getLine().split(" ");
        bankerObj.addMtxClaimC(new ProcessObj(bankerObj.getIdCounter(), new int[bankerObj.getResources()]));
        bankerObj.incProcesses();
        bankerObj.incIdCounter();
        for (int resource = 0; resource < bankerObj.getResources(); resource++) {
            bankerObj.getMtxClaimC().get(bankerObj.getProcesses()-1).setResource(resource, Integer.parseInt(mtxLineArr[resource]));
        }

        mtxLineArr = fileDataInput.getLine().split(" ");
        bankerObj.addMtxAllocationA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
        bankerObj.addMtxNeedCA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
        bankerObj.incIdCounter1();
        for (int resource = 0; resource < bankerObj.getResources(); resource++) {
            bankerObj.getMtxAllocationA().get(bankerObj.getProcesses()-1).setResource(resource, Integer.parseInt(mtxLineArr[resource]));
            bankerObj.getMtxNeedCA().get(bankerObj.getProcesses()-1).setResource(resource,
                    bankerObj.getMtxClaimC().get(bankerObj.getProcesses()-1).getResource(resource) -
                            bankerObj.getMtxAllocationA().get(bankerObj.getProcesses()-1).getResource(resource));
            bankerObj.decAvailableResources(resource, bankerObj.getMtxAllocationA().get(bankerObj.getProcesses()-1).getResource(resource));
        }
        cli.printSingleProcess((bankerObj.getProcesses()-1), bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses());
    }
}
