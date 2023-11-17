import java.util.*;
import java.io.*;

public class ClassificationTree implements Classifier {
    private ClassificationNode overallRoot;

    public ClassificationTree(List<Classifiable> data, List<String> results) {
        if (data.size() != results.size() || data.size() == 0) {
            throw new IllegalArgumentException();
        }
        
        for (int i = 0; i < data.size(); i++) {
            overallRoot = train(overallRoot, data.get(i), results.get(i));
        }
    }

    private ClassificationNode train(ClassificationNode root, Classifiable curr, String result) {
        if (root == null) {
            root = new ClassificationNode(curr, result);
        } else if (root.left == null && root.right == null) {
            if (!root.label.equals(result)) {
                // Need to partition
                Split s = curr.partition(root.data);
                if (s.evaluate(curr)) {
                    // curr belongs on the left
                    root = new ClassificationNode(s, new ClassificationNode(curr, result), root);
                } else {
                    // curr belongs on the right
                    root =  new ClassificationNode(s, root, new ClassificationNode(curr, result));
                }
            }
        } else if (root.evaluate(curr)) {
            root.left = train(root.left, curr, result);
        } else {
            root.right = train(root.right, curr, result);
        }
        return root;
    }

    public ClassificationTree(String fileName) throws FileNotFoundException {
        overallRoot = load(new Scanner(new File(fileName)));
        if (overallRoot == null) {
            throw new IllegalStateException();
        }
    }

    private ClassificationNode load(Scanner sc) {
        String line = sc.nextLine();
        if (!line.contains(Classifiable.SPLITTER)) {
            // Leaf node
            return new ClassificationNode(line);
        } else {
            double threshold = Double.parseDouble(sc.nextLine());
            return new ClassificationNode(new Split(threshold, line), load(sc), load(sc));
        }
    }
    
    public boolean canClassify(Classifiable input) {
        return canClassify(input, overallRoot);
    }

    private boolean canClassify(Classifiable input, ClassificationNode root) {
        if (root != null && root.left != null && root.right != null) {
            return input.getKeys().contains(root.split.getSplittedKey()) &&
                   canClassify(input, root.left) &&
                   canClassify(input, root.right);
        }
        return true;
    }

    public String classify(Classifiable input) {
        return classify(input, overallRoot);
    }

    private String classify(Classifiable input, ClassificationNode root) {
        if (root.left == null && root.right == null) {
            return root.label;
        } else {
            return root.evaluate(input) ? classify(input, root.left) : classify(input, root.right);
        }
    }

    public void save(String fileName) throws FileNotFoundException {
        PrintStream ps = new PrintStream(fileName);
        save(overallRoot, ps);
    }

    private void save(ClassificationNode root, PrintStream ps) {
        if (root.left == null && root.right == null) {
            ps.println(root.label);
        } else {
            ps.println(root.split);
            save(root.left, ps);
            save(root.right, ps);
        }
    }

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
                correct ++;
            }
        }
        return correct / data.size();
    }

    private static class ClassificationNode {
        public Split split;
        public String label;
        public Classifiable data;
        public ClassificationNode left;
        public ClassificationNode right;

        public ClassificationNode(Split split, ClassificationNode left, ClassificationNode right) {
            this.split = split;
            this.left = left;
            this.right = right;
        }

        public ClassificationNode(String label) {
            this(null, label);
        }

        public ClassificationNode(Classifiable data, String label) {
            this.data = data;
            this.label = label;
        }

        public boolean evaluate(Classifiable value) {
            return split.evaluate(value);
        }
    }
}