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
        int processos = bankerObj.getProcesses();

        boolean[] encerradoTemp = new boolean[bankerObj.getProcesses()];
        Arrays.fill(encerradoTemp, Boolean.FALSE);
        List<Boolean> encerrado = new ArrayList<>();
        for (boolean bol : encerradoTemp) {
            encerrado.add(bol);
        }

        boolean allProcessChecked = true;

        while(allProcessChecked) {
            allProcessChecked = false;
            for(int processo = 0; processo < processos; processo++) {
                if(!encerrado.get(processo)) {
                    int recurso;

                    for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                        if(tempMtxNeedCA.get(processo).getResource(recurso) > tempAvailableResources[recurso]) {
                            break;
                        }
                    }

                    if(recurso == bankerObj.getResources()) {

                        for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                            tempAvailableResources[recurso] += tempMtxAllocationA.get(processo).getResource(recurso);

                            if (tempAvailableResources[recurso] > bankerObj.getMaxResource(recurso))
                                tempAvailableResources[recurso] = bankerObj.getMaxResource(recurso);
                        }

                        safeSequence.append("P").append(tempMtxAllocationA.get(processo).getId()).append(", ");
                        tempMtxAllocationA.remove(processo);
                        tempMtxClaimC.remove(processo);
                        tempMtxNeedCA.remove(processo);
                        processos--;
                        encerrado.add(processo, true);
                        allProcessChecked = true;

                    }
                }

                if (encerrado.get(processo))
                    encerrado.remove(processo);

            }
        }

        for(int i = 0; i < processos; i++) {
            if(!encerrado.get(i)) {
                if (safeSequence.length() > 0)
                    cli.printMessage("UNSAFE State a sequencia maxima a ser executada é: " + safeSequence);
                else
                    cli.printMessage("UNSAFE Nao eh possivel executar nenhum processo\n" + safeSequence);
                return false;
            }
        }

        cli.printMessage("SAFE State a sequência a ser executada  é: " + safeSequence.substring(0,
                safeSequence.length()-2) + "\n");
        return true;
    }
}
