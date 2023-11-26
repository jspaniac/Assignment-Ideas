import java.util.*;
import java.io.*;

public abstract class Cipher {
    public static final int MIN_CHAR = (int)(' ');
    public static final int MAX_CHAR = (int)('}');
    public static final int TOTAL_CHARS = MAX_CHAR - MIN_CHAR + 1;

    public void handleFile(String fileName, boolean encode) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));
        String out = fileName.split("\\.txt")[0] + (encode ? "-encoded" : "-decoded") + ".txt";
        PrintStream ps = new PrintStream(out);
        while(sc.hasNextLine()) {
            ps.println(handleInput(sc.nextLine(), encode));
        }
    }

    public abstract String handleInput(String input, boolean encode);
}