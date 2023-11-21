import java.io.*;
import java.util.*;

public abstract class Classifier {
    public abstract boolean canClassify(Classifiable input);

    public abstract String classify(Classifiable input);

    public abstract void save(PrintStream ps);

    public Map<String, Double> calculateAccuracy(List<Classifiable> data, List<String> labels) {
        if (data.size() != labels.size()) {
            throw new IllegalArgumentException();
        }
        
        Map<String, Integer> labelToTotal = new HashMap<>();
        labelToTotal.put("Overall", 0);
        Map<String, Double> labelToCorrect = new HashMap<>();
        labelToCorrect.put("Overall", 0.0);
        
        for (int i = 0; i < data.size(); i++) {
            if (!canClassify(data.get(i))) {
               throw new IllegalArgumentException();
            }

            String result = classify(data.get(i));
            String label = labels.get(i);

            labelToTotal.put(label, labelToTotal.getOrDefault(label, 0) + 1);
            labelToTotal.put("Overall", labelToTotal.get("Overall") + 1);
            if (result.equals(label)) {
                labelToCorrect.put(result, labelToCorrect.getOrDefault(result, 0.0) + 1);
                labelToCorrect.put("Overall", labelToCorrect.get("Overall") + 1);
            }
        }

        for (String label : labelToCorrect.keySet()) {
            labelToCorrect.put(label, labelToCorrect.get(label) / labelToTotal.get(label));
        }
        return labelToCorrect;
    }
}