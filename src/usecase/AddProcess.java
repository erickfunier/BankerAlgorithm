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
        for (int recurso = 0; recurso < bankerObj.getResources(); recurso++) {
            bankerObj.getMtxClaimC().get(bankerObj.getProcesses()-1).setResource(recurso, Integer.parseInt(mtxLineArr[recurso]));
        }

        mtxLineArr = fileDataInput.getLine().split(" ");
        bankerObj.addMtxAllocationA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
        bankerObj.addMtxNeedCA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
        bankerObj.incIdCounter1();
        for (int recurso = 0; recurso < bankerObj.getResources(); recurso++) {
            bankerObj.getMtxAllocationA().get(bankerObj.getProcesses()-1).setResource(recurso, Integer.parseInt(mtxLineArr[recurso]));
            bankerObj.getMtxNeedCA().get(bankerObj.getProcesses()-1).setResource(recurso,
                    bankerObj.getMtxClaimC().get(bankerObj.getProcesses()-1).getResource(recurso) -
                            bankerObj.getMtxAllocationA().get(bankerObj.getProcesses()-1).getResource(recurso));
            bankerObj.decAvailableResources(recurso, bankerObj.getMtxAllocationA().get(bankerObj.getProcesses()-1).getResource(recurso));
        }
        cli.printSingleProcess((bankerObj.getProcesses()-1), bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses());
    }
}
