package thread;

import domain.BankerObj;
import port.Cli;
import port.FileDataInput;

import static usecase.AddProcess.addInstance;

public class ThreadNewProcess extends Thread {
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
                cli.printMessage("\n----------- Adicionando um novo processo -----------");
                addInstance(fileDataInput, cli, bankerObj);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter--;
            cli.printMessage("\n----------- Novo processo adicionado -----------\n");
        }
    }
}
