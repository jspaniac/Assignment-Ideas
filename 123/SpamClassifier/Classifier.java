import java.io.*;
import java.util.*;

public abstract class Classifier {
    public abstract boolean canClassify(Classifiable input);

    public abstract String classify(Classifiable input);

    public abstract void save(PrintStream ps) throws FileNotFoundException;

    public double calculateAccuracy(List<Classifiable> data, List<String> labels) {
        if (data.size() != labels.size()) {
            throw new IllegalArgumentException();
        }
        
        double correct = 0;
        for (int i = 0; i < data.size(); i++) {
            if (!canClassify(data.get(i))) {
               throw new IllegalArgumentException();
            }

            String result = classify(data.get(i));
            if (result.equals(labels.get(i))) {
                correct++;
            }
        }
        return correct / data.size();
    }
}