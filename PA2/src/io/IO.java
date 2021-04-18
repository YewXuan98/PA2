package src.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IO {

    private static final Scanner sc = new Scanner(System.in);

    private static final List<String> inputs = new ArrayList<>();

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

    public static StringBuilder fileReader(String file) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            text.append(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void fileWriter(String path, String nonce){
        try{
            FileWriter myWriter= new FileWriter(path);
            myWriter.write(nonce);
            myWriter.close();
            System.out.println("Successfully wrote into nonce file");
        } catch (IOException e) {
            System.out.println("An error on writing into nonce file");
            e.printStackTrace();
        }
    }


}
