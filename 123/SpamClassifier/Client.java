import java.util.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the CSE 123 Email Classifier! To begin, enter your desired mode of operation:");
        System.out.println();
        System.out.println("1) Train email classification model");
        System.out.println("2) Load model from file");
        System.out.print("Enter your choice here: ");

        int choice = console.nextInt();
        while (choice != 1 && choice != 2) {
            System.out.print("Please enter a valid option from above: ");
            choice = console.nextInt();
        }

        Classifier c = null;
        if (choice == 1) {
            c = trainModel();
        } else if (choice == 2) {
            System.out.print("Please enter the path to the file you'd like to load: ");
            c = new ClassificationTree(console.next());
        }
        // More options to add here depending on extension chosen!

        System.out.println();
        System.out.println("What would you like to do with your model?");
        boolean loop = true;
        while (loop) {
            System.out.println();
            System.out.println("1) Test with an email");
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
                System.out.print("Please enter the email file you'd like to test: ");
                String fileName = console.next();
                Email e = loadEmail(new File(fileName));

                System.out.println("Result: " + c.classify(e));
            } else if (choice == 2) {
                List<Classifiable> spam = new ArrayList<>(loadEmails("./data/test/spam"));
                double spamAccuracy = c.calculateAccuracy(spam, Collections.nCopies(spam.size(), "spam"));

                List<Classifiable> ham = new ArrayList<>(loadEmails("./data/test/ham"));
                double hamAccuracy = c.calculateAccuracy(ham, Collections.nCopies(ham.size(), "ham"));

                double overallAccuracy = ((spamAccuracy * spam.size()) + (hamAccuracy * ham.size())) / (spam.size() + ham.size());
                System.out.println("Spam accuracy   : " + spamAccuracy);
                System.out.println("Ham accuracy    : " + hamAccuracy);
                System.out.println("Overall accuracy: " + overallAccuracy);
            } else if (choice == 3) {
                System.out.print("Please enter the file name you'd like to save to: ");
                String fileName = console.next();
                c.save(fileName);
            } else {
                loop = false;
            }
        }
    }

    private static Classifier trainModel() throws FileNotFoundException {
        List<Email> spam = loadEmails("./data/train/spam");
        List<Email> ham = loadEmails("./data/train/ham");
        
        List<Classifiable> data = new ArrayList<>(spam);
        data.addAll(ham);

        List<String> labels = new ArrayList<>(Collections.nCopies(spam.size(), "spam"));
        labels.addAll(Collections.nCopies(ham.size(), "ham"));

        // Note that we *have* to create two Random objects with the same seed 
        // for both lists to be shuffled the same
        Collections.shuffle(data, new Random(1092032));
        Collections.shuffle(labels, new Random(1092032));

        return new ClassificationTree(data, labels);
    }

    private static List<Email> loadEmails(String directory) throws FileNotFoundException {
        List<Email> emails = new ArrayList<>();
        File dir = new File(directory);
        for (File data : dir.listFiles()) {
            emails.add(loadEmail(data));
        }
        return emails;
    }

    private static Email loadEmail(File f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        String subject = sc.nextLine();
        String content = sc.nextLine();
        return new Email(subject, content);
    }
}