package solution;

import java.util.*;
import java.io.*;

public class Client {
    public static final Cipher CHOSEN_CIPHER = new MultiCipher(List.of(
        new CaesarShift(1), new Transposition(2)
    ));

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the CSE 123 cryptography application!");
        System.out.println("What would you like to do?");
        int chosen = -1;
        do {
            System.out.println();
            System.out.println("(1) Encode / (2) Decode a string");
            System.out.println("(3) Encode / (4) Decode a file");
            System.out.println("(5) Quit");
            System.out.print("Enter your choice here: ");
            
            chosen = Integer.parseInt(console.nextLine());
            while (chosen < 1 || chosen > 5) {
                System.out.print("Please enter a valid option from above: ");
                chosen = Integer.parseInt(console.nextLine());
            }

            if (chosen == 1 || chosen == 2) {
                System.out.println("Please enter the string you'd like to " +
                                    (chosen == 1 ? "encode" : "decode") + ": ");
                String input = console.nextLine();
                System.out.println(CHOSEN_CIPHER.handleInput(input, chosen == 1));
            } else if (chosen == 3 || chosen == 4) {
                System.out.print("Please enter the name of the file you'd like to " +
                                    (chosen == 3 ? "encode" : "decode") + ": ");
                String fileName = console.nextLine();
                CHOSEN_CIPHER.handleFile(fileName, chosen == 3);
            }
        } while (chosen != 5);
    }
}