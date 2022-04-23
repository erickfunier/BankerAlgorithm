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
            safeState = true;

            while(safeState) {
                System.out.println("\nBANKER STARTED\n");
                safeState = checkSafeState(cli, bankerObj, Cli.cliThreadPrint.BANKER);
                if (safeState) {
                    try {
                        checkSafeState2(cli, bankerObj, Cli.cliThreadPrint.BANKER);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (!safeState && !threadNewProcess.isAlive()) {
                    System.out.println("\nBANKER END");
                    return;
                } else {
                    cli.printMessage("\nQuantidade de cada recurso disponivel no momento: \n", Cli.cliThreadPrint.BANKER);
                    cli.printVector("R", bankerObj.getAvailableResources(), Cli.cliThreadPrint.BANKER);
                    cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), -1, Cli.cliThreadPrint.BANKER);

                    System.out.println("\nBANKER WAITING");
                    safeState = true;
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class ThreadNewProcess extends Thread {
        private final FileDataInput fileDataInput;
        private final Cli cli;
        private final BankerObj bankerObj;
        public ThreadNewProcess(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
            this.fileDataInput = fileDataInput;
            this.cli = cli;
            this.bankerObj = bankerObj;
        }
        @Override
        public void run() {
            int counter = 6;

            fileDataInput.runtimeFileName();
            while(counter > 0) {

                try {
                    Thread.sleep(15000);
                    /*if (!checkSafeState(cli, bankerObj))
                        return;*/
                    cli.printMessage("\n----------- Adicionando um novo processo -----------", Cli.cliThreadPrint.NEWPROCESS);
                    addInstance(fileDataInput, cli, bankerObj);


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                counter--;
                System.out.println("\n----------- Novo processo adicionado -----------");
            }
        }
    }

    private static boolean checkSafeState2(Cli cli, BankerObj bankerObj, Cli.cliThreadPrint cliThreadPrint) throws InterruptedException {
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
                        boolean unsafe = checkSafeState(cli, bankerObj, cliThreadPrint);
                        if (!unsafe)
                            return false;
                        newProcessAdded = false;
                    }

                    int recurso;

                    for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                        if(bankerObj.getMtxNeedCA().get(processo).getResource(recurso) > bankerObj.getAvailableResource(recurso)) {
                            break;
                        }
                    }

                    if(recurso == bankerObj.getResources()) {
                        cli.printMessage("Quantidade de cada recurso disponivel no momento: \n", cliThreadPrint);
                        cli.printVector("R", bankerObj.getAvailableResources(), cliThreadPrint);
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), processo, cliThreadPrint);

                        safeSequence.append("P" + bankerObj.getMtxAllocationA().get(processo).getId() + ", ");
                        cli.printMessage("\n--> P" + bankerObj.getMtxAllocationA().get(processo).getId() + " em execucao\n", cliThreadPrint);
                        for(recurso = 0; recurso < bankerObj.getResources(); recurso++) {
                            bankerObj.setAvailableResource(recurso, bankerObj.getAvailableResource(recurso) - bankerObj.getMtxNeedCA().get(processo).getResource(recurso));
                            bankerObj.getMtxAllocationA().get(processo).setResource(recurso, bankerObj.getMtxAllocationA().get(processo).getResource(recurso) + bankerObj.getMtxNeedCA().get(processo).getResource(recurso));
                            bankerObj.getMtxNeedCA().get(processo).setResource(recurso, 0);
                        }

                        cli.printMessage("\nQuantidade de cada recurso disponivel no momento: \n", Cli.cliThreadPrint.BANKER);
                        cli.printVector("R", bankerObj.getAvailableResources(), Cli.cliThreadPrint.BANKER);
                        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), processo, cliThreadPrint);

                        Thread.sleep(10000);
                        if (bankerObj.getProcesses() > startedProcesses)
                            newProcessAdded = true;
                        cli.printMessage("\n--> P" + bankerObj.getMtxAllocationA().get(processo).getId() + " terminou execucao\n\n", cliThreadPrint);
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
                        bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), -1, cliThreadPrint);
                cli.printMessage("\nUNSAFE State " + safeSequence, cliThreadPrint);
                return false;
            }
        }

        cli.printMessage("SAFE State a sequência executada foi: " + safeSequence, cliThreadPrint);
        return true;
    }

    private static boolean checkSafeState(Cli cli, BankerObj bankerObj, Cli.cliThreadPrint cliThreadPrint) {
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
                cli.printMessage("UNSAFE State a sequência máxima a ser executada  é: " + safeSequence, cliThreadPrint);
                return false;
            }
        }

        if (bankerObj.getProcesses() == 0) {
            cli.printMessage("UNSAFE State a sequência máxima a ser executada  é: " + safeSequence, cliThreadPrint);
            return false;
        }

        cli.printMessage("SAFE State a sequência a ser executada  é: " + safeSequence, cliThreadPrint);
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

        cli.printMessage("\n\nQuantidade de cada recurso disponivel no momento: \n", Cli.cliThreadPrint.DEFAULT);
        cli.printVector("R", bankerObj.getAvailableResources(), Cli.cliThreadPrint.DEFAULT);
        cli.printSystemStatus(bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(),
                bankerObj.getResources(), bankerObj.getProcesses(), -1, Cli.cliThreadPrint.DEFAULT);

    }

    private static void addInstance(FileDataInput fileDataInput, Cli cli, BankerObj bankerObj) {
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
        cli.printSingleProcess((bankerObj.getProcesses()-1), bankerObj.getMtxClaimC(), bankerObj.getMtxAllocationA(), bankerObj.getMtxNeedCA(), bankerObj.getResources(), bankerObj.getProcesses(), Cli.cliThreadPrint.NEWPROCESS);
    }

    public static void main(String[] args) throws NumberFormatException{
        FileDataInput fileDataInput = new FileDataInput("runtimeFile.txt", "starterFile.txt");
        Cli cli = new Cli();

        int processes = fileDataInput.getInt();
        cli.printMessage("Numero de processos: " + processes, Cli.cliThreadPrint.DEFAULT);

        int resources = fileDataInput.getInt();
        cli.printMessage("\nNumero de recursos: " + resources, Cli.cliThreadPrint.DEFAULT);

        int[] maxRecursos = new int[resources];

        for (int recurso = 0; recurso < resources; recurso++) {
            maxRecursos[recurso] = fileDataInput.getInt();
        }

        cli.printMessage("\nQuantidade maxima de cada recurso disponivel: \n", Cli.cliThreadPrint.DEFAULT);
        cli.printVector("R", maxRecursos, Cli.cliThreadPrint.DEFAULT);

        BankerObj bankerObj = new BankerObj(processes, resources, maxRecursos);

        loadInstance(fileDataInput, cli, bankerObj);
        //boolean safeState = checkSafeState(cli, bankerObj);

        //if (safeState) {
            ThreadNewProcess threadNewProcess = new ThreadNewProcess(fileDataInput, cli, bankerObj);
            RunBanker runBanker = new RunBanker(cli, bankerObj, threadNewProcess);
            runBanker.start();
            threadNewProcess.start();
        //}




    }
}