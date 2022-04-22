import domain.BankerObj;
import domain.ProcessObj;
import util.Cli;
import util.FileDataInput;

import java.util.*;

public class Main {

    private static BankerObj loadInstance(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
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
            bankerObj.decAvailableResource(recurso,
                    bankerObj.getMaxResource(recurso) - bankerObj.getAvailableResource(recurso));

        cli.printMessage("\n\nQuantidade de cada recurso disponivel no momento: \n");
        cli.printVector("R", bankerObj.getAvailableResources());
        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(),
                bankerObj.getResources(), bankerObj.getProcesses(), -1);

        return bankerObj;
    }

    public static void main(String[] args) throws NumberFormatException{
        FileDataInput fileDataInput = new FileDataInput("starterFile.txt", "runtimeFile.txt");
        Cli cli = new Cli();

        int processes = fileDataInput.getInt();
        int idCounter = 0;
        int idCounter1 = 0;
        cli.printMessage("Numero de processos: " + processes);

        int resources = fileDataInput.getInt();
        cli.printMessage("\nNumero de recursos: " + resources);

        //int[] recursosDisponiveis = new int[resources];
        int[] maxRecursos = new int[resources];
        /*List<ProcessObj> mtxAllocationA = new ArrayList<>();
        List<ProcessObj> mtxClaimC = new ArrayList<>();
        List<ProcessObj> mtxNeedCA = new ArrayList<>();*/
        StringBuilder safeSequence = new StringBuilder();

        for (int i = 0; i < resources; i++) {
            maxRecursos[i] = fileDataInput.getInt();
        }

        cli.printMessage("\nQuantidade maxima de cada recurso disponivel: \n");
        cli.printVector("R", maxRecursos);

        BankerObj bankerObj = new BankerObj(processes, resources, maxRecursos);

        loadInstance(fileDataInput, cli, bankerObj);

        boolean[] encerradoTemp = new boolean[processes];
        Arrays.fill(encerradoTemp, Boolean.FALSE);
        List<Boolean> encerrado = new ArrayList<>();
        for (boolean bol : encerradoTemp) {
            encerrado.add(bol);
        }

        int count = 0;
        cli.printMessage("\n");

        while(count < processes) {
            for(int processo = 0; processo < processes; processo++) {

                if(!encerrado.get(processo)) {
                    int recurso;

                    for(recurso = 0; recurso < resources; recurso++) {
                        if(bankerObj.getMtxNeedCA().get(processo).getResource(recurso) > bankerObj.getAvailableResource(recurso)) {
                            break; //No allocation
                        }
                    }

                    //If all processes are allocated
                    if(recurso == resources) {
                        cli.printMessage("\n\nQuantidade de cada recurso disponivel no momento: \n");
                        cli.printVector("R", bankerObj.getAvailableResources());
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(), resources, processes, processo);

                        for(recurso = 0; recurso < resources; recurso++) {
                            bankerObj.incAvailableResources(recurso, bankerObj.getMtxAllocationA().get(processo).getResource(recurso));

                            if (bankerObj.getAvailableResource(recurso) > maxRecursos[recurso])
                                bankerObj.setAvailableResource(recurso, maxRecursos[recurso]);
                        }

                        safeSequence.append("P" + bankerObj.getMtxAllocationA().get(processo).getId() + ", ");
                        bankerObj.removeProcess(processo);
                        processes--;
                        encerrado.add(processo, true);
                        count++;
                    }
                }
                if (encerrado.get(processo))
                    encerrado.remove(processo);
            }
        }

        for(int i = 0; i < processes; i++) {
            if(!encerrado.get(i)) {
                cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                        bankerObj.getMtxNeedCA(), resources, processes, -1);
                cli.printMessage("\nUNSAFE " + safeSequence);
                return;
            }
        }

        cli.printMessage("\nSAFE And Sequence is: " + safeSequence);
    }
}