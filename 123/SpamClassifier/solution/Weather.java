import java.util.*;

// Simple Weather implementation using just temp and humidity

public class Weather extends Classifiable {
    public static final List<String> FEATURES = Arrays.asList("temp", "humidity");
    private static final double MAX_TEMP = 38.0;

    private double temp;
    private double humidity;

    public Weather(double temp, double humidity) {
        this.temp = temp;
        this.humidity = humidity;
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
            return this.getTempPercentage();
        } else if (index == 1) {
            return this.humidity;
        }
        
        return 0.0;
    }

    private double getTempPercentage() {
        // Note that negatives diffs scale even greater since we don't have a min temp
        // (good since negative temps should majorly factor into label [rain vs. snow])
        return Math.min(this.temp / MAX_TEMP, 1.0);
    }

    public Split partition(Classifiable other) {
        if (!(other instanceof Weather)) {
            throw new IllegalArgumentException();
        }

        Weather otherWeather = (Weather) other;

        double bestDiff = 0;
        String bestFeature = null;
        for (String feature : FEATURES) {
            double diff = Math.abs(this.get(feature) - otherWeather.get(feature));
            if (bestFeature == null || diff > bestDiff) {
                bestDiff = diff;
                bestFeature = feature;
            }
        }

        return new Split(Split.midpoint(this.get(bestFeature), otherWeather.get(bestFeature)),
                         bestFeature);
    }
}