package usecase;

import domain.BankerObj;
import port.Cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static usecase.CheckSafeState.checkSafeState;

public class ExecuteBanker {
    public static void executeBanker(Cli cli, BankerObj bankerObj) throws InterruptedException {
        StringBuilder safeSequence = new StringBuilder();

        boolean[] finishedTemp = new boolean[bankerObj.getProcesses()];
        Arrays.fill(finishedTemp, Boolean.FALSE);
        List<Boolean> finished = new ArrayList<>();
        for (boolean bol : finishedTemp) {
            finished.add(bol);
        }

        boolean allProcessChecked = true;
        boolean newProcessAdded = false;
        int startedProcesses = bankerObj.getProcesses();

        while(allProcessChecked) {
            allProcessChecked = false;
            for(int process = 0; process < bankerObj.getProcesses(); process++) {
                if(!finished.get(process)) {
                    if (newProcessAdded) {
                        boolean unsafe = checkSafeState(cli, bankerObj);
                        if (!unsafe)
                            return;
                        newProcessAdded = false;
                    }

                    int resources;

                    for(resources = 0; resources < bankerObj.getResources(); resources++) {
                        if(bankerObj.getMtxNeedCA().get(process).getResource(resources) > bankerObj.getAvailableResource(resources)) {
                            break;
                        }
                    }

                    if(resources == bankerObj.getResources()) {
                        cli.printMessage("Available resources: \n");
                        cli.printVector("R", bankerObj.getAvailableResources());
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                                bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), process);

                        safeSequence.append("P").append(bankerObj.getMtxAllocationA().get(process).getId()).append(", ");
                        cli.printMessage("\n--> P" + bankerObj.getMtxAllocationA().get(process).getId() + " executing\n");

                        for(resources = 0; resources < bankerObj.getResources(); resources++) {
                            bankerObj.setAvailableResource(resources,
                                    bankerObj.getAvailableResource(resources) -
                                            bankerObj.getMtxNeedCA().get(process).getResource(resources));
                            bankerObj.getMtxAllocationA().get(process).setResource(resources,
                                    bankerObj.getMtxAllocationA().get(process).getResource(resources) +
                                            bankerObj.getMtxNeedCA().get(process).getResource(resources));
                            bankerObj.getMtxNeedCA().get(process).setResource(resources, 0);
                        }

                        cli.printMessage("\nAvailable resources: \n");
                        cli.printVector("R", bankerObj.getAvailableResources());
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                                bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), process);

                        Thread.sleep(10000);
                        if (bankerObj.getProcesses() > startedProcesses)
                            newProcessAdded = true;
                        cli.printMessage("\n--> P" + bankerObj.getMtxAllocationA().get(process).getId() + " executed\n\n");

                        for(resources = 0; resources < bankerObj.getResources(); resources++) {
                            bankerObj.incAvailableResources(resources, bankerObj.getMtxClaimC().get(process).getResource(resources));

                            if (bankerObj.getAvailableResource(resources) > bankerObj.getMaxResource(resources))
                                bankerObj.setAvailableResource(resources, bankerObj.getMaxResource(resources));
                        }

                        bankerObj.removeProcess(process);
                        bankerObj.decProcesses();
                        finished.add(process, true);
                        allProcessChecked = true;
                        startedProcesses = bankerObj.getProcesses();

                        if (newProcessAdded)
                            process = 0;
                    }
                }

                if (finished.get(process)) {
                    finished.remove(process);
                }

            }
        }

        for(int i = 0; i < bankerObj.getProcesses(); i++) {
            if(!finished.get(i)) {
                cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                        bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), -1);
                cli.printMessage("\nUNSAFE State " + safeSequence);
                return;
            }
        }

        cli.printMessage("SAFE State executed sequence: " + safeSequence);
    }
}
