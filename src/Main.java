import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static void printSystemStatus(int mtxAllocation[][], int mtxMaxAllocation[][], int mtxNeed[][], int recursos, int processos, int usedProcess) {
        System.out.print("\n\n\tAllocation\t\t\t\t|\tMax Allocation\t\t\t|\tNeed\t\t\t\t\t|\n");
        for (int i = 0, j = 0; j < 3*recursos; i++, j++) {
            System.out.print("\tR" + i);
            if (i == recursos-1) {
                i = -1;
                System.out.print("\t|");
            }

        }
        for(int i = 0; i < processos; i++) {
            System.out.print("\nP" + i);
            for (int j = 0; j < recursos; j++) {
                if (usedProcess == i)
                    System.out.print("\t\033[7m\033[4m"+mtxAllocation[i][j]+"\033[0m");
                else
                    System.out.print("\t"+mtxAllocation[i][j]);
            }
            System.out.print("\t|");
            for (int j = 0; j < recursos; j++) {
                if (usedProcess == i)
                    System.out.print("\t\033[3m\033[4m"+mtxMaxAllocation[i][j]+"\033[0m");
                else
                    System.out.print("\t"+mtxMaxAllocation[i][j]);

            }
            System.out.print("\t|");
            for (int j = 0; j < recursos; j++) {
                if (usedProcess == i)
                    System.out.print("\t\033[3m\033[4m"+mtxNeed[i][j]+"\033[0m");
                else
                    System.out.print("\t"+mtxNeed[i][j]);
            }
            System.out.print("\t|");
        }
    }

    public static void main(String[] args) throws IOException, NumberFormatException{

        FileReader fileReader = new FileReader("input.txt");
        Scanner scanner = new Scanner(fileReader);

        int processos = scanner.nextInt();
        System.out.print("Numero de processos: " + processos);

        int recursos = scanner.nextInt();
        System.out.print("\nNumero de recursos: " + recursos);
        scanner.nextLine();

        int recursosDisponiveis[] = new int[recursos];
        int mtxMaxAllocation[][] = new int[processos][recursos];
        int mtxAllocation[][] = new int[processos][recursos];
        int mtxNeed[][] = new int[processos][recursos];
        String safeSequence = "";

        System.out.print("\nRecursos disponiveis de cada processo: \n");
        String mtxLineArr[];

        for (int i = 0; i < recursos; i++)
            System.out.print("P" + i + "\t");
        System.out.print("\n");
        for (int i = 0; i < recursos; i++) {
            recursosDisponiveis[i] = scanner.nextInt();
            System.out.print(recursosDisponiveis[i] + "\t");
        }

        scanner.nextLine();
        for (int i = 0; i < processos; i++) {
            String mtxLine = scanner.nextLine();
            mtxLineArr = mtxLine.split(" ");
            for (int j = 0; j < recursos; j++) {
                mtxAllocation[i][j] = Integer.parseInt(mtxLineArr[j]);
            }
        }

        scanner.nextLine();
        for (int i = 0; i < processos; i++) {
            String mtxLine = scanner.nextLine();
            mtxLineArr = mtxLine.split(" ");
            for (int j = 0; j < recursos; j++) {
                mtxMaxAllocation[i][j] = Integer.parseInt(mtxLineArr[j]);
                mtxNeed[i][j] = mtxMaxAllocation[i][j] - mtxAllocation[i][j];
            }
        }

        printSystemStatus(mtxAllocation, mtxMaxAllocation, mtxNeed, recursos, processos, -1);

        /*System.out.print("\n\n\tAllocation\t\t\t\t|\tMax Allocation\t\t\t|\tNeed\t\t\t\t\t|\n");
        for (int i = 0, j = 0; j < 3*recursos; i++, j++) {
            System.out.print("\tR" + i);
            if (i == recursos-1) {
                i = -1;
                System.out.print("\t|");
            }

        }
        for(int i = 0; i < processos; i++) {
            System.out.print("\nP" + i);
            for (int j = 0; j < recursos; j++) {
                System.out.print("\t"+mtxAllocation[i][j]);
            }
            System.out.print("\t|");
            for (int j = 0; j < recursos; j++) {
                System.out.print("\t"+mtxMaxAllocation[i][j]);
            }
            System.out.print("\t|");
            for (int j = 0; j < recursos; j++) {
                System.out.print("\t\033[3m\033[4m"+mtxNeed[i][j]+"\033[0m");
            }
            System.out.print("\t|");
        }*/

        boolean encerrado[] = new boolean[processos];
        Arrays.fill(encerrado, Boolean.FALSE);

        boolean check = true;
        System.out.print("\n");
        //Until all process allocated
        while(check) {
            check = false;
            for(int processo = 0; processo < processos; processo++) {
                //Trying to allocate
                if(!encerrado[processo]) {
                    int recurso;

                    for(recurso = 0; recurso < recursos; recurso++) {
                        if(mtxNeed[processo][recurso] > recursosDisponiveis[recurso]) {
                            break; //No allocation
                        }
                    }

                    //If all processes are allocated
                    if(recurso == recursos) {
                        for(recurso = 0; recurso < recursos; recurso++) {
                            recursosDisponiveis[recurso] = recursosDisponiveis[recurso] + mtxAllocation[processo][recurso];
                            printSystemStatus(mtxAllocation, mtxMaxAllocation, mtxNeed, recursos, processos, processo);
                        }
                        System.out.println("Processo escolhido: P" + processo);

                        encerrado[processo] = true;
                        check = true;
                        safeSequence += processo + ", ";
                    }
                }
            }
        }

        int i;
        for(i = 0; i < processos; i++) {
            if(!encerrado[i]) {
                System.out.print("\nDEAD LOCK");
                return;
            }
        }

        System.out.print("\nSAFE And Sequence is: " + safeSequence);
    }
}