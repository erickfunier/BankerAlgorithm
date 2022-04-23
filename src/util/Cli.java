package util;

import domain.ProcessObj;

import java.util.List;

public class Cli {

    public enum cliThreadPrint {
        BANKER,
        NEWPROCESS,
        DEFAULT
    }

    public void printVector(String vectorName, int[] vector, cliThreadPrint cliThreadPrint) {

        for (int i = 0; i < vector.length; i++)
            printMessage(vectorName + i + "\t", cliThreadPrint);
        printMessage("\n", cliThreadPrint);
        for (int i = 0; i < vector.length; i++) {
            printMessage(vector[i] + "\t", cliThreadPrint);
        }
    }

    public void printSystemStatus(List<ProcessObj> mtxClaimC, List<ProcessObj> mtxAllocationA, List<ProcessObj> mtxNeedCA, int recursos, int processos, int usedProcess, cliThreadPrint cliThreadPrint) {
        switch (cliThreadPrint) {
            case BANKER:
                System.out.print("\033[103m");
                break;
            case NEWPROCESS:
                System.out.print("\033[106m");
                break;
        }

        System.out.print("\n\n\tClaim C\t\t\t\t\t|\tAllocation A\t\t\t|\tNeed C-A\t\t\t\t|\n");
        for (int i = 0, j = 0; j < 3*recursos; i++, j++) {
            System.out.print("\tR" + i);
            if (i == recursos-1) {
                i = -1;
                System.out.print("\t|");
            }

        }
        for(int processo = 0; processo < processos; processo++) {
            if (usedProcess == processo) {
                System.out.print("\n\033[3m\033[4mP"+mtxClaimC.get(processo).getId()+"\033[0m");
                switch (cliThreadPrint) {
                    case BANKER:
                        System.out.print("\033[103m");
                        break;
                    case NEWPROCESS:
                        System.out.print("\033[106m");
                        break;
                }
            } else {
                System.out.print("\nP" + mtxClaimC.get(processo).getId());
            }

            for (int recurso = 0; recurso < recursos; recurso++) {
                if (usedProcess == processo) {
                    System.out.print("\t\033[3m\033[4m"+mtxClaimC.get(processo).getResource(recurso)+"\033[0m");
                    switch (cliThreadPrint) {
                        case BANKER:
                            System.out.print("\033[103m");
                            break;
                        case NEWPROCESS:
                            System.out.print("\033[106m");
                            break;
                    }
                }
                else
                    System.out.print("\t"+mtxClaimC.get(processo).getResource(recurso));
            }
            System.out.print("\t|");
            for (int recurso = 0; recurso < recursos; recurso++) {
                if (usedProcess == processo) {
                    System.out.print("\t\033[3m\033[4m"+mtxAllocationA.get(processo).getResource(recurso)+"\033[0m");
                    switch (cliThreadPrint) {
                        case BANKER:
                            System.out.print("\033[103m");
                            break;
                        case NEWPROCESS:
                            System.out.print("\033[106m");
                            break;
                    }
                }
                else
                    System.out.print("\t"+mtxAllocationA.get(processo).getResource(recurso));

            }
            System.out.print("\t|");
            for (int recurso = 0; recurso < recursos; recurso++) {
                if (usedProcess == processo) {
                    System.out.print("\t\033[3m\033[4m"+mtxNeedCA.get(processo).getResource(recurso)+"\033[0m");
                    switch (cliThreadPrint) {
                        case BANKER:
                            System.out.print("\033[103m");
                            break;
                        case NEWPROCESS:
                            System.out.print("\033[106m");
                            break;
                    }
                }

                else
                    System.out.print("\t"+mtxNeedCA.get(processo).getResource(recurso));
            }
            System.out.print("\t|");
        }
        System.out.print("\n____________________________________________________________________________________\n");
    }

    public void printSingleProcess(int processo, List<ProcessObj> mtxClaimC, List<ProcessObj> mtxAllocationA, List<ProcessObj> mtxNeedCA, int recursos, int usedProcess, cliThreadPrint cliThreadPrint) {
        switch (cliThreadPrint) {
            case BANKER:
                System.out.print("\033[103m");
                break;
            case NEWPROCESS:
                System.out.print("\033[106m");
                break;
        }

        System.out.print("\n\n\tClaim C\t\t\t\t\t|\tAllocation A\t\t\t|\tNeed C-A\t\t\t\t|\n");
        for (int i = 0, j = 0; j < 3*recursos; i++, j++) {
            System.out.print("\tR" + i);
            if (i == recursos-1) {
                i = -1;
                System.out.print("\t|");
            }

        }

        if (usedProcess == processo) {
            System.out.print("\n\033[3m\033[4mP"+mtxClaimC.get(processo).getId()+"\033[0m");
            switch (cliThreadPrint) {
                case BANKER:
                    System.out.print("\033[103m");
                    break;
                case NEWPROCESS:
                    System.out.print("\033[106m");
                    break;
            }
        } else {
            System.out.print("\nP" + mtxClaimC.get(processo).getId());
        }

        for (int recurso = 0; recurso < recursos; recurso++) {
            if (usedProcess == processo) {
                System.out.print("\t\033[3m\033[4m"+mtxClaimC.get(processo).getResource(recurso)+"\033[0m");
                switch (cliThreadPrint) {
                    case BANKER:
                        System.out.print("\033[103m");
                        break;
                    case NEWPROCESS:
                        System.out.print("\033[106m");
                        break;
                }
            }
            else
                System.out.print("\t"+mtxClaimC.get(processo).getResource(recurso));
        }
        System.out.print("\t|");
        for (int recurso = 0; recurso < recursos; recurso++) {
            if (usedProcess == processo) {
                System.out.print("\t\033[3m\033[4m"+mtxAllocationA.get(processo).getResource(recurso)+"\033[0m");
                switch (cliThreadPrint) {
                    case BANKER:
                        System.out.print("\033[103m");
                        break;
                    case NEWPROCESS:
                        System.out.print("\033[106m");
                        break;
                }
            }
            else
                System.out.print("\t"+mtxAllocationA.get(processo).getResource(recurso));

        }
        System.out.print("\t|");
        for (int recurso = 0; recurso < recursos; recurso++) {
            if (usedProcess == processo) {
                System.out.print("\t\033[3m\033[4m"+mtxNeedCA.get(processo).getResource(recurso)+"\033[0m");
                switch (cliThreadPrint) {
                    case BANKER:
                        System.out.print("\033[103m");
                        break;
                    case NEWPROCESS:
                        System.out.print("\033[106m");
                        break;
                }
            }

            else
                System.out.print("\t"+mtxNeedCA.get(processo).getResource(recurso));
        }
        System.out.print("\t|");

        System.out.print("\n____________________________________________________________________________________\n");
    }

    public void printMessage(String message, cliThreadPrint cliThreadPrint) {
        switch (cliThreadPrint) {
            case BANKER:
                System.out.print("\033[103m");
                System.out.print(message);
                //System.out.print("\033[0m");
                break;
            case NEWPROCESS:
                System.out.print("\033[106m");
                System.out.print(message);
                //System.out.print("\033[0m");
                break;
            case DEFAULT:
                System.out.print("\033[0m");
                System.out.print(message);
                break;
        }


    }
}
