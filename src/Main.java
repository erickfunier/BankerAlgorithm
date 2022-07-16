import domain.BankerObj;
import thread.ThreadRunBanker;
import thread.ThreadNewProcess;
import port.Cli;
import port.FileDataInput;

import static usecase.LoadProcesses.loadInstance;

public class Main {
    public static void main(String[] args) throws NumberFormatException{
        // change the following paths to use other cases
        String starterFileName = "case2/starterFile.txt";
        String runtimeFileName = "case2/runtimeFile.txt";

        FileDataInput fileDataInput = new FileDataInput(runtimeFileName, starterFileName);
        Cli cli = new Cli();

        int processes = fileDataInput.getInt();
        cli.printMessage("Number of process: " + processes);

        int resources = fileDataInput.getInt();
        cli.printMessage("\nNumber of resources: " + resources);

        int[] maxResources = new int[resources];

        for (int resource = 0; resource < resources; resource++) {
            maxResources[resource] = fileDataInput.getInt();
        }

        cli.printMessage("\nMaximum available resources: \n");
        cli.printVector("R", maxResources);

        BankerObj bankerObj = new BankerObj(processes, resources, maxResources);

        loadInstance(fileDataInput, cli, bankerObj);

        ThreadNewProcess threadNewProcess = new ThreadNewProcess(fileDataInput, cli, bankerObj);
        ThreadRunBanker runBanker = new ThreadRunBanker(cli, bankerObj, threadNewProcess);
        runBanker.start();
        threadNewProcess.start();
    }
}