package port;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FileDataInput {
    private String starterFilename;
    private String runtimeFilename;
    private FileReader fileReader;
    private Scanner scanner;

    public FileDataInput(String runtimeFilename, String starterFilename) {
        this.runtimeFilename = runtimeFilename;
        this.starterFilename = starterFilename;

        try {
            if (fileReader != null) {
                fileReader.close();
            }
            fileReader = new FileReader(starterFilename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        scanner = new Scanner(fileReader);
    }

    public void runtimeFileName() {
        try {
            if (fileReader != null) {
                fileReader.close();
            }
            fileReader = new FileReader(runtimeFilename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        scanner = new Scanner(fileReader);
    }

    public int getInt() {

        return scanner.nextInt();
    }

    public String getLine() {
        String tempString = scanner.nextLine();
        if (tempString.isEmpty()) {
            return scanner.nextLine();
        }
        return tempString;

    }
}
