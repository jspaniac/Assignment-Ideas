public class Split {
    private double threshold;
    private String key;

    public Split(double threshold, String key) {
        this.threshold = threshold;
        this.key = key;
    }

    public double getThreshold() {
        return threshold;
    }

    public String getKey() {
        return key;
    }

    public String getSplittedKey() {
        return key.split(Classifiable.SPLITTER)[0];
    }

    public boolean evaluate(Classifiable value) {
        return value.get(this.key) < this.threshold;
    }

    public String toString() {
        return this.key + "\n" + this.threshold;
    }
}