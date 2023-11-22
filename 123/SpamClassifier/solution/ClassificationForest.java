import java.util.*;
import java.io.*;

public class ClassificationForest extends Classifier {
    private List<ClassificationTree> forest;

    public ClassificationForest(int n, List<Classifiable> data, List<String> labels) {
        if (n == 0) {
            throw new IllegalArgumentException();
        }
        forest = new ArrayList<>(n);
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int seed = rand.nextInt(Integer.MAX_VALUE);
            Collections.shuffle(data, new Random(seed));
            Collections.shuffle(labels, new Random(seed));
            forest.add(new ClassificationTree(data, labels));
        }
    }

    public ClassificationForest(Scanner sc) {
        int n = Integer.parseInt(sc.nextLine());
        forest = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            forest.add(new ClassificationTree(sc));
        }
    }

    public boolean canClassify(Classifiable input) {
        return forest.get(0).canClassify(input);
    }

    public String classify(Classifiable input) {
        Map<String, Integer> counts = new HashMap<>();
        for (ClassificationTree tree : forest) {
            String label = tree.classify(input);
            if (!counts.containsKey(label)) {
                counts.put(label, 0);
            }
            counts.put(label, counts.get(label) + 1);
        }

        String best = null;
        for (String label : counts.keySet()) {
            if (best == null || counts.get(label) > counts.get(best)) {
                best = label;
            }
        }
        return best;
    }

    public void save(PrintStream ps) {
        ps.println(forest.size());
        for (ClassificationTree tree : forest) {
            tree.save(ps);
        }
    }
}