package src.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomFileReader {

    private static final Scanner sc = new Scanner(System.in);

    private static final List<String> inputs = new ArrayList<>();

    public static final String
        DIRECTORY = "./";

    public static List<String> readFile() {
        System.out.println("Please enter file name:");
        System.out.println("Please type exit if you want to exit file:");
        String input;
        do {
            input = sc.nextLine();
            inputs.add(input);
        } while (!input.equals("exit"));
        return inputs;
    }

    public static StringBuilder readFileFromList(String file) {
        StringBuilder text = new StringBuilder();
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(String.format("%s%s", DIRECTORY, file)));
            while ((line = bufferedReader.readLine()) != null) {
                text.append("\n").append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }


}
