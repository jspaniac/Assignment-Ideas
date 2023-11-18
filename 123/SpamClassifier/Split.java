public class Split {
    private double threshold;
    private String feature;

    public Split(double threshold, String key) {
        this.threshold = threshold;
        this.feature = feature;
    }

    public double getThreshold() {
        return threshold;
    }

    public String getFeature() {
        return feature.split(Classifiable.SPLITTER)[0];
    }

    public boolean evaluate(Classifiable value) {
        return value.get(this.feature) < this.threshold;
    }

    public String toString() {
        return this.feature + "\n" + this.threshold;
    }
}