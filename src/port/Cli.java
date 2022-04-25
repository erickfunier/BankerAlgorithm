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
                                  List<ProcessObj> mtxNeedCA, int recursos, int processos, int usedProcess) {
        System.out.print("\n\n\t\tClaim C\t\t\t\t\t|\tAllocation A\t\t\t|\tNeed C-A\t\t\t\t|\n\t");
        for (int i = 0, j = 0; j < 3*recursos; i++, j++) {
            System.out.print("\tR" + i);
            if (i == recursos-1) {
                i = -1;
                System.out.print("\t|");
            }

        }
        for(int processo = 0; processo < processos; processo++) {
            if (usedProcess == processo) {
                System.out.print("\n -->P"+mtxClaimC.get(processo).getId());
            } else {
                System.out.print("\n\tP" + mtxClaimC.get(processo).getId());
            }

            for (int recurso = 0; recurso < recursos; recurso++) {
                System.out.print("\t"+mtxClaimC.get(processo).getResource(recurso));
            }
            System.out.print("\t|");
            for (int recurso = 0; recurso < recursos; recurso++) {
                System.out.print("\t"+mtxAllocationA.get(processo).getResource(recurso));
            }
            System.out.print("\t|");
            for (int recurso = 0; recurso < recursos; recurso++) {
                System.out.print("\t"+mtxNeedCA.get(processo).getResource(recurso));
            }
            System.out.print("\t|");
        }
        System.out.print("\n\t____________________________________________________________________________________\n");
    }

    public void printSingleProcess(int processo, List<ProcessObj> mtxClaimC, List<ProcessObj> mtxAllocationA,
                                   List<ProcessObj> mtxNeedCA, int recursos, int usedProcess) {
        System.out.print("\n\n\t\tClaim C\t\t\t\t\t|\tAllocation A\t\t\t|\tNeed C-A\t\t\t\t|\n\t");
        for (int i = 0, j = 0; j < 3*recursos; i++, j++) {
            System.out.print("\tR" + i);
            if (i == recursos-1) {
                i = -1;
                System.out.print("\t|");
            }

        }

        if (usedProcess == processo) {
            System.out.print("\n -->P"+mtxClaimC.get(processo).getId());
        } else {
            System.out.print("\n\tP" + mtxClaimC.get(processo).getId());
        }

        for (int recurso = 0; recurso < recursos; recurso++) {
            System.out.print("\t"+mtxClaimC.get(processo).getResource(recurso));
        }
        System.out.print("\t|");
        for (int recurso = 0; recurso < recursos; recurso++) {
            System.out.print("\t"+mtxAllocationA.get(processo).getResource(recurso));

        }
        System.out.print("\t|");
        for (int recurso = 0; recurso < recursos; recurso++) {
            System.out.print("\t"+mtxNeedCA.get(processo).getResource(recurso));
        }
        System.out.print("\t|\n\t____________________________________________________________________________________\n");
    }

    public void printMessage(String message) {
        System.out.print(message);
    }
}
