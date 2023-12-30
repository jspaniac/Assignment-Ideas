import java.util.*;

// Simple Song implementation using just danceability and energy

public class Song extends Classifiable {
    public static final List<String> FEATURES = Arrays.asList("dance", "energy");

    private double dance;
    private double energy;

    public Song(double dance, double energy) {
        this.dance = dance;
        this.energy = energy;
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
            return this.dance;
        } else if (index == 1) {
            return this.energy;
        }
        
        return 0.0;
    }

    public Split partition(Classifiable other) {
        if (!(other instanceof Song)) {
            throw new IllegalArgumentException();
        }

        Song otherSong = (Song) other;

        double bestDiff = 0;
        String bestFeature = null;
        for (String feature : FEATURES) {
            double diff = Math.abs(this.get(feature) - otherSong.get(feature));
            if (bestFeature == null || diff > bestDiff) {
                bestDiff = diff;
                bestFeature = feature;
            }
        }

        return new Split(Split.midpoint(this.get(bestFeature), otherSong.get(bestFeature)),
                         bestFeature);
    }
}