import java.util.*;

public class Email extends Classifiable {
    public static final List<String> FEATURES = Arrays.asList("wordPercent");
    
    private Map<String, Integer> words;
    private double totalWords;

    public Email(String content) {
        this.words = new HashMap<>();
        parseContent(content);
    }

    private void parseContent(String content) {
        Scanner sc = new Scanner(content);
        while (sc.hasNext()) {
            String word = sc.next();
            if (!words.containsKey(word)) {
                words.put(word, 0);
            }
            words.put(word, words.get(word) + 1);
            totalWords++;
        }
    }

    public List<String> getFeatures() {
        return FEATURES;
    }

    public double get(String feature) {
        String[] splitted = feature.split(Classifiable.SPLITTER);

        int index = FEATURES.indexOf(splitted[0]);
        if (index == -1) {
            throw new IllegalArgumentException();
        }
        
        if (index == 0) {
            // Words
            return getWordPercentage(splitted[1]);
        }
        return 0.0;
    }

    private double getWordPercentage(String word) {
        return totalWords == 0 ? 0.0 : this.words.getOrDefault(word, 0) / totalWords;
    }

    public Split partition(Classifiable other) {
        if (!(other instanceof Email)) {
            throw new IllegalArgumentException();
        }

        Email otherEmail = (Email) other;

        // Pick partition that maximizes difference
        Set<String> allWords = new HashSet<>(this.words.keySet());
        allWords.addAll(otherEmail.words.keySet());

        String bestWord = null;
        double highestDiff = 0;
        for (String word : allWords) {
            double diff = Math.abs(this.getWordPercentage(word) - otherEmail.getWordPercentage(word));
            if (diff > highestDiff) {
                bestWord = word;
                highestDiff = diff;
            }
        }

        // Calculate halfway between the two points
        double halfway = Math.min(this.getWordPercentage(bestWord), otherEmail.getWordPercentage(bestWord)) + (highestDiff / 2);
        return new Split(halfway, FEATURES.get(0) + Classifiable.SPLITTER + bestWord);
    }
}