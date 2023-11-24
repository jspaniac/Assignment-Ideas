import java.util.*;
import java.util.function.*;
import java.io.*;

public class DataLoader {
    private List<Classifiable> data;
    private List<String> labels;

    public DataLoader(String filePath, int labelIndex,
                      Function<List<String>, Classifiable> toClassifiable)
                      throws FileNotFoundException {
        this.data = new ArrayList<>();
        this.labels = new ArrayList<>();
        List<List<String>> rows = CsvReader.readCsv(filePath);
        for (List<String> row : rows) {
            this.data.add(toClassifiable.apply(row));
            this.labels.add(row.get(labelIndex));
        }
        DataLoader.shuffle(this);
    }

    public List<Classifiable> getData() {
        return this.data;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public static final Random RAND = new Random();
    public static void shuffle(DataLoader loader) {
        DataLoader.shuffle(loader.data, loader.labels);
    }

    public static void shuffle(List<Classifiable> data, List<String> labels) {
        int seed = RAND.nextInt(Integer.MAX_VALUE);
        Collections.shuffle(data, new Random(seed));
        Collections.shuffle(labels, new Random(seed));
    }
}