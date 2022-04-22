import domain.BankerObj;
import domain.ProcessObj;
import util.Cli;
import util.FileDataInput;

import java.util.*;

public class Main {

    static class RunBanker extends Thread {
        private final Cli cli;
        private final BankerObj bankerObj;
        private boolean safeState;
        private ThreadNewProcess threadNewProcess;
        public RunBanker(Cli cli, BankerObj bankerObj, ThreadNewProcess threadNewProcess) {
            this.cli = cli;
            this.bankerObj = bankerObj;
            this.safeState = true;
            this.threadNewProcess = threadNewProcess;
        }
        @Override
        public void run() {
            safeState = checkSafeState(cli, bankerObj);

            while(safeState) {
                System.out.println("\nBANKER START");
                try {
                    safeState = checkSafeState2(cli, bankerObj);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("\nBANKER END");
                if (!safeState)
                    return;
                else if (bankerObj.getProcesses() == 0 && threadNewProcess.isAlive()) {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return;
                }

            }
        }
    }

    static class ThreadNewProcess extends Thread {
        private final FileDataInput fileDataInput;
        private final Cli cli;
        private final BankerObj bankerObj;
        private boolean safeState;
        public ThreadNewProcess(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
            this.fileDataInput = fileDataInput;
            this.cli = cli;
            this.bankerObj = bankerObj;
            this.safeState = true;
        }
        @Override
        public void run() {
            int counter = 6;
            safeState = true;


            fileDataInput.runtimeFileName();
            while(counter > 0) {

                try {
                    Thread.sleep(15000);
                    if (!checkSafeState(cli, bankerObj))
                        return;
                    System.out.println("\nSAFE\n-----------NEW START");
                    addInstance(fileDataInput, cli, bankerObj);


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                counter--;
                System.out.println("\n-----------NEW END");
            }
        }
    }

    private static boolean checkSafeState2(Cli cli, BankerObj bankerObj) throws InterruptedException {
        StringBuilder safeSequence = new StringBuilder();

        boolean[] encerradoTemp = new boolean[bankerObj.getProcesses()];
        Arrays.fill(encerradoTemp, Boolean.FALSE);
        List<Boolean> encerrado = new ArrayList<>();
        for (boolean bol : encerradoTemp) {
            encerrado.add(bol);
        }

        boolean allProcessChecked = true;
        //cli.printMessage("\n");

        while(allProcessChecked) {
            allProcessChecked = false;
            for(int processo = 0; processo < bankerObj.getProcesses(); processo++) {
                if(!encerrado.get(processo)) {
                    int recurso;

                    for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                        if(bankerObj.getMtxNeedCA().get(processo).getResource(recurso) > bankerObj.getAvailableResource(recurso)) {
                            break;
                        }
                    }

                    if(recurso == bankerObj.getResources()) {
                        cli.printMessage("\nQuantidade de cada recurso disponivel no momento: \n");
                        cli.printVector("R", bankerObj.getAvailableResources());
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), processo);

                        safeSequence.append("P" + bankerObj.getMtxAllocationA().get(processo).getId() + ", ");
                        System.out.println("\nP" + bankerObj.getMtxAllocationA().get(processo).getId() + " em execucao");
                        for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                            bankerObj.setAvailableResource(recurso, bankerObj.getAvailableResource(recurso) - bankerObj.getMtxNeedCA().get(processo).getResource(recurso));
                        }
                        Thread.sleep(10000);
                        System.out.println("\nP" + bankerObj.getMtxAllocationA().get(processo).getId() + " terminou execucao");
                        for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                            bankerObj.incAvailableResources(recurso, bankerObj.getMtxClaimC().get(processo).getResource(recurso));

                            if (bankerObj.getAvailableResource(recurso) > bankerObj.getMaxResource(recurso))
                                bankerObj.setAvailableResource(recurso, bankerObj.getMaxResource(recurso));
                        }

                        //safeSequence.append("P" + bankerObj.getMtxAllocationA().get(processo).getId() + ", ");
                        //System.out.println("\nP" + bankerObj.getMtxAllocationA().get(processo).getId() + " em execucao");


                        bankerObj.removeProcess(processo);
                        bankerObj.decProcesses();
                        encerrado.add(processo, true);
                        allProcessChecked = true;

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
                cli.printMessage("\nUNSAFE " + safeSequence);
                return false;
            }
        }

        cli.printMessage("\nSAFE And Sequence is: " + safeSequence);
        return true;
    }

    private static boolean checkSafeState(Cli cli, BankerObj bankerObj) {
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
        //cli.printMessage("\n");

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
                        /*cli.printMessage("\n\npQuantidade de cada recurso disponivel no momento: \n");
                        cli.printVector("R", tempAvailableResources);
                        cli.printSystemStatus(tempMtxClaimC, tempMtxAllocationA, tempMtxNeedCA, bankerObj.getResources(), processos, processo);*/

                        for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                            tempAvailableResources[recurso] += tempMtxAllocationA.get(processo).getResource(recurso);

                            if (tempAvailableResources[recurso] > bankerObj.getMaxResource(recurso))
                                tempAvailableResources[recurso] = bankerObj.getMaxResource(recurso);
                        }

                        safeSequence.append("P" + tempMtxAllocationA.get(processo).getId() + ", ");
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
                /*cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(),
                        bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), -1);*/
                //cli.printMessage("\nUNSAFE " + safeSequence);
                return false;
            }
        }

        cli.printMessage("\n\npQuantidade de cada recurso disponivel no momento: \n");
        cli.printVector("R", bankerObj.getAvailableResources());
        //cli.printMessage("\nSAFE And Sequence is: " + safeSequence);
        return true;
    }

    private static void loadInstance(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
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

    private static void addInstance(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
        /*cli.printMessage("\n\noQuantidade de cada recurso disponivel no momento: \n");
        cli.printVector("R", bankerObj.getAvailableResources());*/

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

       /* cli.printMessage("\n\nQuantidade de cada recurso disponivel no momento: \n");
        cli.printVector("R", bankerObj.getAvailableResources());
        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(),
                bankerObj.getResources(), bankerObj.getProcesses(), -1);*/

    }

    public static void main(String[] args) throws NumberFormatException{
        FileDataInput fileDataInput = new FileDataInput("runtimeFile.txt", "starterFile.txt");
        Cli cli = new Cli();

        int processes = fileDataInput.getInt();
        cli.printMessage("Numero de processos: " + processes);

        int resources = fileDataInput.getInt();
        cli.printMessage("\nNumero de recursos: " + resources);

        int[] maxRecursos = new int[resources];

        for (int recurso = 0; recurso < resources; recurso++) {
            maxRecursos[recurso] = fileDataInput.getInt();
        }

        cli.printMessage("\nQuantidade maxima de cada recurso disponivel: \n");
        cli.printVector("R", maxRecursos);



        BankerObj bankerObj = new BankerObj(processes, resources, maxRecursos);

        loadInstance(fileDataInput, cli, bankerObj);
        boolean safeState = checkSafeState(cli, bankerObj);

        if (safeState) {
            ThreadNewProcess threadNewProcess = new ThreadNewProcess(fileDataInput, cli, bankerObj);
            RunBanker runBanker = new RunBanker(cli, bankerObj, threadNewProcess);
            runBanker.start();
            threadNewProcess.start();
        }




    }
}