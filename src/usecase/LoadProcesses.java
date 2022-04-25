package usecase;

import domain.BankerObj;
import domain.ProcessObj;
import port.Cli;
import port.FileDataInput;

public class LoadProcesses {
    public static void loadInstance(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
        for (int processo = 0; processo < bankerObj.getProcesses(); processo++) {
            String[] mtxLineArr = fileDataInput.getLine().split(" ");
            bankerObj.addMtxClaimC(new ProcessObj(bankerObj.getIdCounter(), new int[bankerObj.getResources()]));
            bankerObj.incIdCounter();
            for (int recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                bankerObj.getMtxClaimC().get(processo).setResource(recurso, Integer.parseInt(mtxLineArr[recurso]));
            }
        }

        for (int processo = 0; processo < bankerObj.getProcesses(); processo++) {
            String[] mtxLineArr = fileDataInput.getLine().split(" ");
            bankerObj.addMtxAllocationA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
            bankerObj.addMtxNeedCA(new ProcessObj(bankerObj.getIdCounter1(), new int[bankerObj.getResources()]));
            bankerObj.incIdCounter1();

            for (int recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                bankerObj.getMtxAllocationA().get(processo).setResource(recurso, Integer.parseInt(mtxLineArr[recurso]));
                bankerObj.getMtxNeedCA().get(processo).setResource(recurso,bankerObj.getMtxClaimC().get(processo).getResource(recurso) -
                        bankerObj.getMtxAllocationA().get(processo).getResource(recurso));
                bankerObj.incAvailableResources(recurso, bankerObj.getMtxAllocationA().get(processo).getResource(recurso));
            }
        }

        for (int recurso = 0; recurso < bankerObj.getResources(); recurso++)
            bankerObj.setAvailableResource(recurso,
                    bankerObj.getMaxResource(recurso) - bankerObj.getAvailableResource(recurso));

        cli.printMessage("\n\nQuantidade de cada recurso disponivel no momento: \n");
        cli.printVector("R", bankerObj.getAvailableResources());
        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(),
                bankerObj.getResources(), bankerObj.getProcesses(), -1);

    }
}
