package util;

import domain.ProcessObj;

import java.util.List;

public class Cli {

    public void printVector(String vectorName, int[] vector) {
        for (int i = 0; i < vector.length; i++)
            printMessage(vectorName + i + "\t");
        printMessage("\n");
        for (int i = 0; i < vector.length; i++) {
            printMessage(vector[i] + "\t");
        }
    }

    public void printSystemStatus(List<ProcessObj> mtxClaimC, List<ProcessObj> mtxAllocationA, List<ProcessObj> mtxNeedCA, int recursos, int processos, int usedProcess) {
        System.out.print("\n\n\tClaim C\t\t\t\t\t|\tAllocation A\t\t\t|\tNeed C-A\t\t\t\t|\n");
        for (int i = 0, j = 0; j < 3*recursos; i++, j++) {
            System.out.print("\tR" + i);
            if (i == recursos-1) {
                i = -1;
                System.out.print("\t|");
            }

        }
        for(int processo = 0; processo < processos; processo++) {
            if (usedProcess == processo)
                System.out.print("\n\033[3m\033[4mP"+mtxClaimC.get(processo).getId()+"\033[0m");
            else
                System.out.print("\nP" + mtxClaimC.get(processo).getId());
            for (int recurso = 0; recurso < recursos; recurso++) {
                if (usedProcess == processo)
                    System.out.print("\t\033[3m\033[4m"+mtxClaimC.get(processo).getResource(recurso)+"\033[0m");
                else
                    System.out.print("\t"+mtxClaimC.get(processo).getResource(recurso));
            }
            System.out.print("\t|");
            for (int recurso = 0; recurso < recursos; recurso++) {
                if (usedProcess == processo)
                    System.out.print("\t\033[3m\033[4m"+mtxAllocationA.get(processo).getResource(recurso)+"\033[0m");
                else
                    System.out.print("\t"+mtxAllocationA.get(processo).getResource(recurso));

            }
            System.out.print("\t|");
            for (int recurso = 0; recurso < recursos; recurso++) {
                if (usedProcess == processo)
                    System.out.print("\t\033[3m\033[4m"+mtxNeedCA.get(processo).getResource(recurso)+"\033[0m");
                else
                    System.out.print("\t"+mtxNeedCA.get(processo).getResource(recurso));
            }
            System.out.print("\t|");
        }
        System.out.print("\n____________________________________________________________________________________");
    }

    public void printMessage(String message) {
        System.out.print(message);
    }
}
