package starting;
import java.util.*;
import java.io.*;

public abstract class Classifiable {
    public static final String SPLITTER = "~";
    public static final String DICT_PATH = "./data/dict.txt";

    public static Set<String> loadDict(String fileName) {
        Set<String> dict = new HashSet<>();
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNextLine()) {
                String word = sc.nextLine();
                dict.add(word);
            }
        } finally {
            return dict;
        }
    }
    // For use in an extension, if chosen!
    public static final Set<String> DICT = Classifiable.loadDict(DICT_PATH);

    public abstract double get(String feature);
    public abstract List<String> getFeatures();
    public abstract Split partition(Classifiable other);
}