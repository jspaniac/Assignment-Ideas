import java.util.*;
import java.io.*;

public class CsvReader {
    public static final String COMMA = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static List<List<String>> readCsv(String fileName) throws FileNotFoundException {
        List<List<String>> lines = new ArrayList<>();
        
        Scanner sc = new Scanner(new File(fileName));
        // Skip the first row since it's just titles
        sc.nextLine();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            lines.add(Arrays.asList(line.split(COMMA)));
        }
        return lines;
    }
}