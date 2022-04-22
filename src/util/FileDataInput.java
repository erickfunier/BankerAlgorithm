package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class FileDataInput {
    private String starterFilename;
    private String runtimeFilename;
    private FileReader fileReader;
    private Scanner scanner;

    public FileDataInput(String starterFilename, String runtimeFilename) {
        this.starterFilename = starterFilename;
        this.runtimeFilename = runtimeFilename;

        try {
            fileReader = new FileReader(starterFilename);
        } catch (FileNotFoundException e) {
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
