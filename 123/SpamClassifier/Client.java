import java.util.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the CSE 123 Email Classifier! To begin, enter your desired mode of operation:");
        System.out.println();
        System.out.println("1) Train email classification model");
        System.out.println("2) Load model from file");
        //System.out.println("3) Train random forest");
        System.out.print("Enter your choice here: ");

        int choice = console.nextInt();
        while (choice != 1 && choice != 2 /* && choice != 3 */) {
            System.out.print("Please enter a valid option from above: ");
            choice = console.nextInt();
        }

        Classifier c = null;
        if (choice == 1) {
            c = trainModel(true, -1);
        } else if (choice == 2) {
            System.out.print("Please enter the path to the file you'd like to load: ");
            String fileName = console.next();
            c = new ClassificationTree(new Scanner(new File(fileName)));
        }
        // else if (choice == 3) {
        //     System.out.print("How many trees would you like in the forest: ");
        //     int n = console.nextInt();
        //     c = trainModel(false, n);
        // }

        System.out.println();
        System.out.println("What would you like to do with your model?");
        boolean loop = true;
        while (loop) {
            System.out.println();
            System.out.println("1) Test with an input file");
            System.out.println("2) Get testing accuracy");
            System.out.println("3) Save to a file");
            System.out.println("4) Quit");
            System.out.print("Enter your choice here: ");

            choice = console.nextInt();
            while (choice != 1 && choice != 2 && choice != 3 && choice != 4) {
                System.out.print("Please enter a valid option from above: ");
                choice = console.nextInt();
            }

            if (choice == 1) {
                System.out.print("Please enter the file you'd like to test: ");
                String fileName = console.next();
                System.out.println("Result: " + evalModel(c, fileName));
            } else if (choice == 2) {
                testModel(c);
            } else if (choice == 3) {
                System.out.print("Please enter the file name you'd like to save to: ");
                String fileName = console.next();
                c.save(new PrintStream(fileName));
            } else {
                loop = false;
            }
        }
    }

    private static Classifier trainModel(boolean tree, int n) throws FileNotFoundException {
        // TODO: Change this if doing the non-email extension!
        List<List<String>> train = CsvReader.readCsv("data/emails/train.csv");
        List<Classifiable> data = new ArrayList<>(toEmails(train));
        List<String> labels = getLabels(train, 0);

        int seed = new Random().nextInt(Integer.MAX_VALUE);
        Collections.shuffle(data, new Random(seed));
        Collections.shuffle(labels, new Random(seed));

        // if (!tree) {
        //     return new ClassificationForest(n, data, labels);
        // }
        return new ClassificationTree(data, labels);
    }

    private static void testModel(Classifier c) throws FileNotFoundException {
        // TODO: Change this if doing the non-email extension!
        List<List<String>> test = CsvReader.readCsv("data/emails/test.csv");
        List<Classifiable> data = new ArrayList<>(toEmails(test));
        List<String> labels = getLabels(test, 0);

        Map<String, Double> labelToAccuracy = c.calculateAccuracy(data, labels);
        
        for (String label : labelToAccuracy.keySet()) {
            System.out.println(label + ": " + labelToAccuracy.get(label));
        }
    }

    private static List<String> evalModel(Classifier c, String fileName) throws FileNotFoundException {
        // TODO: Change this if doing the non-email extension!
        List<List<String>> input = CsvReader.readCsv(fileName);
        List<Classifiable> data = new ArrayList<>(toEmails(input));

        List<String> labels = new ArrayList<>();
        for (Classifiable x : data) {
            labels.add(c.classify(x));
        }
        return labels;
    }

    private static List<Email> toEmails(List<List<String>> input) {
        List<Email> emails = new ArrayList<>();
        for (List<String> row : input) {
            emails.add(new Email(row.get(1)));
        }
        return emails;
    }

    private static List<String> getLabels(List<List<String>> input, int labelIndex) {
        List<String> labels = new ArrayList<>();
        for (List<String> row : input) {
            labels.add(row.get(labelIndex));
        }
        return labels;
    }
}