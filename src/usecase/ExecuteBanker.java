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

        boolean[] encerradoTemp = new boolean[bankerObj.getProcesses()];
        Arrays.fill(encerradoTemp, Boolean.FALSE);
        List<Boolean> encerrado = new ArrayList<>();
        for (boolean bol : encerradoTemp) {
            encerrado.add(bol);
        }

        boolean allProcessChecked = true;
        boolean newProcessAdded = false;
        int startedProcesses = bankerObj.getProcesses();

        while(allProcessChecked) {
            allProcessChecked = false;
            for(int processo = 0; processo < bankerObj.getProcesses(); processo++) {
                if(!encerrado.get(processo)) {
                    if (newProcessAdded) {
                        boolean unsafe = checkSafeState(cli, bankerObj);
                        if (!unsafe)
                            return;
                        newProcessAdded = false;
                    }

                    int recurso;

                    for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                        if(bankerObj.getMtxNeedCA().get(processo).getResource(recurso) > bankerObj.getAvailableResource(recurso)) {
                            break;
                        }
                    }

                    if(recurso == bankerObj.getResources()) {
                        cli.printMessage("Quantidade de cada recurso disponivel no momento: \n");
                        cli.printVector("R", bankerObj.getAvailableResources());
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                                bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), processo);

                        safeSequence.append("P").append(bankerObj.getMtxAllocationA().get(processo).getId()).append(", ");
                        cli.printMessage("\n--> P" + bankerObj.getMtxAllocationA().get(processo).getId() + " em execucao\n");

                        for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                            bankerObj.setAvailableResource(recurso,
                                    bankerObj.getAvailableResource(recurso) -
                                            bankerObj.getMtxNeedCA().get(processo).getResource(recurso));
                            bankerObj.getMtxAllocationA().get(processo).setResource(recurso,
                                    bankerObj.getMtxAllocationA().get(processo).getResource(recurso) +
                                            bankerObj.getMtxNeedCA().get(processo).getResource(recurso));
                            bankerObj.getMtxNeedCA().get(processo).setResource(recurso, 0);
                        }

                        cli.printMessage("\nQuantidade de cada recurso disponivel no momento: \n");
                        cli.printVector("R", bankerObj.getAvailableResources());
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                                bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), processo);

                        Thread.sleep(10000);
                        if (bankerObj.getProcesses() > startedProcesses)
                            newProcessAdded = true;
                        cli.printMessage("\n--> P" + bankerObj.getMtxAllocationA().get(processo).getId() + " terminou execucao\n\n");

                        for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                            bankerObj.incAvailableResources(recurso, bankerObj.getMtxClaimC().get(processo).getResource(recurso));

                            if (bankerObj.getAvailableResource(recurso) > bankerObj.getMaxResource(recurso))
                                bankerObj.setAvailableResource(recurso, bankerObj.getMaxResource(recurso));
                        }

                        bankerObj.removeProcess(processo);
                        bankerObj.decProcesses();
                        encerrado.add(processo, true);
                        allProcessChecked = true;
                        startedProcesses = bankerObj.getProcesses();

                        if (newProcessAdded)
                            processo = 0;
                    }
                }

                if (encerrado.get(processo)) {
                    encerrado.remove(processo);
                }

            }
        }

        for(int i = 0; i < bankerObj.getProcesses(); i++) {
            if(!encerrado.get(i)) {
                cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                        bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), -1);
                cli.printMessage("\nUNSAFE State " + safeSequence);
                return;
            }
        }

        cli.printMessage("SAFE State a sequência executada foi: " + safeSequence);
    }
}
