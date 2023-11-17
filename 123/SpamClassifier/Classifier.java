import java.io.*;
import java.util.*;

public interface Classifier {
    public boolean canClassify(Classifiable input);

    public String classify(Classifiable input);

    public void save(String fileName) throws FileNotFoundException;

    public double calculateAccuracy(List<Classifiable> data, List<String> labels);
}