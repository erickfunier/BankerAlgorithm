import domain.ProcessObj;
import util.Cli;
import util.FileDataInput;

import java.util.*;

public class Main {



    public static void main(String[] args) throws NumberFormatException{
        FileDataInput fileDataInput = new FileDataInput("starterFile.txt", "runtimeFile.txt");
        Cli cli = new Cli();

        int processes = fileDataInput.getInt();
        int idCounter = 0;
        int idCounter1 = 0;
        cli.printMessage("Numero de processos: " + processes);

        int resources = fileDataInput.getInt();
        cli.printMessage("\nNumero de recursos: " + resources);

        int[] recursosDisponiveis = new int[resources];
        int[] maxRecursos = new int[resources];
        List<ProcessObj> mtxAllocationA = new ArrayList<>();
        List<ProcessObj> mtxClaimC = new ArrayList<>();
        List<ProcessObj> mtxNeedCA = new ArrayList<>();
        StringBuilder safeSequence = new StringBuilder();

        for (int i = 0; i < resources; i++) {
            maxRecursos[i] = fileDataInput.getInt();
        }

        cli.printMessage("\nQuantidade maxima de cada recurso disponivel: \n");
        cli.printVector("R", maxRecursos);

        for (int processo = 0; processo < processes; processo++) {
            String[] mtxLineArr = fileDataInput.getLine().split(" ");
            mtxClaimC.add(new ProcessObj(idCounter, new int[resources]));
            idCounter++;
            for (int recurso = 0; recurso < resources; recurso++) {
                mtxClaimC.get(processo).setResource(recurso, Integer.parseInt(mtxLineArr[recurso]));
            }
        }

        for (int processo = 0; processo < processes; processo++) {
            String[] mtxLineArr = fileDataInput.getLine().split(" ");
            mtxAllocationA.add(new ProcessObj(idCounter1, new int[resources]));
            mtxNeedCA.add(new ProcessObj(idCounter1, new int[resources]));
            idCounter1++;
            for (int recurso = 0; recurso < resources; recurso++) {
                mtxAllocationA.get(processo).setResource(recurso, Integer.parseInt(mtxLineArr[recurso]));
                mtxNeedCA.get(processo).setResource(recurso,(mtxClaimC.get(processo).getResource(recurso) -
                        mtxAllocationA.get(processo).getResource(recurso)));
                recursosDisponiveis[recurso] += mtxAllocationA.get(processo).getResource(recurso);
            }

        }
        for (int recurso = 0; recurso < resources; recurso++)
            recursosDisponiveis[recurso] = maxRecursos[recurso] - recursosDisponiveis[recurso];

        cli.printMessage("\n\nQuantidade de cada recurso disponivel no momento: \n");
        cli.printVector("R", recursosDisponiveis);
        cli.printSystemStatus(mtxClaimC, mtxAllocationA, mtxNeedCA, resources, processes, -1);

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
                        if(mtxNeedCA.get(processo).getResource(recurso) > recursosDisponiveis[recurso]) {
                            break; //No allocation
                        }
                    }

                    //If all processes are allocated
                    if(recurso == resources) {
                        cli.printMessage("\n\nQuantidade de cada recurso disponivel no momento: \n");
                        cli.printVector("R", recursosDisponiveis);
                        cli.printSystemStatus(mtxClaimC, mtxAllocationA, mtxNeedCA, resources, processes, processo);
                        for(recurso = 0; recurso < resources; recurso++) {
                            recursosDisponiveis[recurso] += mtxAllocationA.get(processo).getResource(recurso);

                            if (recursosDisponiveis[recurso] > maxRecursos[recurso])
                                recursosDisponiveis[recurso] = maxRecursos[recurso];
                        }

                        safeSequence.append("P" + mtxAllocationA.get(processo).getId() + ", ");
                        mtxClaimC.remove(processo);
                        mtxAllocationA.remove(processo);
                        mtxNeedCA.remove(processo);
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
                cli.printSystemStatus(mtxClaimC, mtxAllocationA, mtxNeedCA, resources, processes, -1);
                cli.printMessage("\nUNSAFE " + safeSequence);
                return;
            }
        }

        cli.printMessage("\nSAFE And Sequence is: " + safeSequence);
    }
}