package port;

import domain.ProcessObj;

import java.util.List;

public class Cli {
    public void printVector(String vectorName, int[] vector) {
        for (int i = 0; i < vector.length; i++)
            printMessage(vectorName + i + "\t");
        printMessage("\n");
        for (int j : vector) {
            printMessage(j + "\t");
        }
    }

    public void printSystemStatus(List<ProcessObj> mtxClaimC, List<ProcessObj> mtxAllocationA,
                                  List<ProcessObj> mtxNeedCA, int resources, int processes, int usedProcess) {
        System.out.print("\n\n\t\tClaim C\t\t\t\t\t|\tAllocation A\t\t\t|\tNeed C-A\t\t\t\t|\n\t");
        for (int i = 0, j = 0; j < 3*resources; i++, j++) {
            System.out.print("\tR" + i);
            if (i == resources-1) {
                i = -1;
                System.out.print("\t|");
            }

        }
        for(int process = 0; process < processes; process++) {
            if (usedProcess == process) {
                System.out.print("\n -->P"+mtxClaimC.get(process).getId());
            } else {
                System.out.print("\n\tP" + mtxClaimC.get(process).getId());
            }

            for (int resource = 0; resource < resources; resource++) {
                System.out.print("\t"+mtxClaimC.get(process).getResource(resource));
            }
            System.out.print("\t|");
            for (int resource = 0; resource < resources; resource++) {
                System.out.print("\t"+mtxAllocationA.get(process).getResource(resource));
            }
            System.out.print("\t|");
            for (int resource = 0; resource < resources; resource++) {
                System.out.print("\t"+mtxNeedCA.get(process).getResource(resource));
            }
            System.out.print("\t|");
        }
        System.out.print("\n\t____________________________________________________________________________________\n");
    }

    public void printSingleProcess(int process, List<ProcessObj> mtxClaimC, List<ProcessObj> mtxAllocationA,
                                   List<ProcessObj> mtxNeedCA, int resources, int usedProcess) {
        System.out.print("\n\n\t\tClaim C\t\t\t\t\t|\tAllocation A\t\t\t|\tNeed C-A\t\t\t\t|\n\t");
        for (int i = 0, j = 0; j < 3*resources; i++, j++) {
            System.out.print("\tR" + i);
            if (i == resources-1) {
                i = -1;
                System.out.print("\t|");
            }

        }

        if (usedProcess == process) {
            System.out.print("\n -->P"+mtxClaimC.get(process).getId());
        } else {
            System.out.print("\n\tP" + mtxClaimC.get(process).getId());
        }

        for (int resource = 0; resource < resources; resource++) {
            System.out.print("\t"+mtxClaimC.get(process).getResource(resource));
        }
        System.out.print("\t|");
        for (int resource = 0; resource < resources; resource++) {
            System.out.print("\t"+mtxAllocationA.get(process).getResource(resource));

        }
        System.out.print("\t|");
        for (int resource = 0; resource < resources; resource++) {
            System.out.print("\t"+mtxNeedCA.get(process).getResource(resource));
        }
        System.out.print("\t|\n\t____________________________________________________________________________________\n");
    }

    public void printMessage(String message) {
        System.out.print(message);
    }
}
