import domain.BankerObj;
import thread.ThreadRunBanker;
import thread.ThreadNewProcess;
import port.Cli;
import port.FileDataInput;

import static usecase.LoadProcesses.loadInstance;

public class Main {
    public static void main(String[] args) throws NumberFormatException{
        String starterFileName = "caso2/starterFile.txt";
        String runtimeFileName = "caso2/runtimeFile.txt";

        FileDataInput fileDataInput = new FileDataInput(runtimeFileName, starterFileName);
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

        ThreadNewProcess threadNewProcess = new ThreadNewProcess(fileDataInput, cli, bankerObj);
        ThreadRunBanker runBanker = new ThreadRunBanker(cli, bankerObj, threadNewProcess);
        runBanker.start();
        threadNewProcess.start();
    }
}