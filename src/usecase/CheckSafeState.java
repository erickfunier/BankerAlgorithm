package usecase;

import domain.BankerObj;
import domain.ProcessObj;
import port.Cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckSafeState {
    public static boolean checkSafeState(Cli cli, BankerObj bankerObj) {
        StringBuilder safeSequence = new StringBuilder();
        List<ProcessObj> tempMtxAllocationA = new ArrayList<>(bankerObj.getMtxAllocationA());
        List<ProcessObj> tempMtxNeedCA = new ArrayList<>(bankerObj.getMtxNeedCA());
        List<ProcessObj> tempMtxClaimC = new ArrayList<>(bankerObj.getMtxClaimC());
        int[] tempAvailableResources = bankerObj.getAvailableResources().clone();
        int processes = bankerObj.getProcesses();

        boolean[] finishedTemp = new boolean[bankerObj.getProcesses()];
        Arrays.fill(finishedTemp, Boolean.FALSE);
        List<Boolean> finished = new ArrayList<>();
        for (boolean bol : finishedTemp) {
            finished.add(bol);
        }

        boolean allProcessChecked = true;

        while(allProcessChecked) {
            allProcessChecked = false;
            for(int process = 0; process < processes; process++) {
                if(!finished.get(process)) {
                    int resource;

                    for(resource = 0; resource < bankerObj.getResources(); resource++) {
                        if(tempMtxNeedCA.get(process).getResource(resource) > tempAvailableResources[resource]) {
                            break;
                        }
                    }

                    if(resource == bankerObj.getResources()) {

                        for(resource = 0; resource < bankerObj.getResources(); resource++) {
                            tempAvailableResources[resource] += tempMtxAllocationA.get(process).getResource(resource);

                            if (tempAvailableResources[resource] > bankerObj.getMaxResource(resource))
                                tempAvailableResources[resource] = bankerObj.getMaxResource(resource);
                        }

                        safeSequence.append("P").append(tempMtxAllocationA.get(process).getId()).append(", ");
                        tempMtxAllocationA.remove(process);
                        tempMtxClaimC.remove(process);
                        tempMtxNeedCA.remove(process);
                        processes--;
                        finished.add(process, true);
                        allProcessChecked = true;

                    }
                }

                if (finished.get(process))
                    finished.remove(process);

            }
        }

        for(int i = 0; i < processes; i++) {
            if(!finished.get(i)) {
                if (safeSequence.length() > 0)
                    cli.printMessage("UNSAFE State the maximum sequence to be executed is: " + safeSequence);
                else
                    cli.printMessage("UNSAFE no one process can be executed\n" + safeSequence);
                return false;
            }
        }

        cli.printMessage("SAFE State the sequence to be executed is: " + safeSequence.substring(0,
                safeSequence.length()-2) + "\n");
        return true;
    }
}
