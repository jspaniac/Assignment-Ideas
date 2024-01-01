import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ExceptionProviders {
    // Caeser.java - Invalid shifter strings with explanation
    public static Stream<TestProviders.Tuple<String, String>> invalidShifterProvider() {
        return Stream.of(
            new TestProviders.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "No encryption"),
            new TestProviders.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ", "Too few characters"),
            new TestProviders.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} ", "Too many characters"),
            new TestProviders.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "Duplicate characters"),
            new TestProviders.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MAX_CHAR + 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside upper range"),
            new TestProviders.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MIN_CHAR - 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside lower range")
        );
    }

    // CaeserShift.java - The number of multiples of (Cipher.MAX_VALUE - Cipher.MIN_VALUE) to test shifting by
    public static final int NUM_MULTIPLES = 100;

    // CaeserKey.java - Invalid keys with explanations
    public static Stream<TestProviders.Tuple<String, String>> invalidKeyProvider() {
        return Stream.of(
            new TestProviders.Tuple<>("", "No characters provided"),
            new TestProviders.Tuple<>("aa", "Duplicate characters"),
            new TestProviders.Tuple<>(" ", "Up to Cipher.MIN_CHAR"),
            new TestProviders.Tuple<>(" !", "Up to Cipher.MIN_CHAR + 1"),
            new TestProviders.Tuple<>(" !\"", "Up to Cipher.MIN_CHAR + 2"),
            new TestProviders.Tuple<>(" !\"#", "Up to Cipher.MIN_CHAR + 3")
        );
    }

    // Concealment.java - Invalid filler values
    public static final int MIN_POSITION = -100;
    public static Stream<Integer> invalidPositionProvider() {
        return IntStream.range(MIN_POSITION, 1).boxed();
    }

    // Vigenere.java - Invalid keys (empty string / just Cipher.MIN_CHAR repeated)
    public static final int MAX_VIGENERE = 100;
    public static Stream<String> invalidVigenereProvider() {
        return IntStream.range(0, MAX_VIGENERE).boxed().map(i -> new StringBuilder().repeat((char) Cipher.MIN_CHAR, i).toString());
    }

    // Transposition.java - Copies fillers but adds a 1
    public static Stream<Integer> invalidTranspositionProvider() {
        return Stream.concat(invalidPositionProvider(), Stream.of(1));
    }
    
    public static final int INVALID_WIDTH = 3;
    public static Stream<String> invalidTranspositionDecryptProvider() {
        return Stream.of("a", "ab", "abcd", "abcde", "abcdefg", "abcdefgh", "abcdefghij", "abcdefghijk");
    }

    // CaesarRandom.java - Tries values outside the range [1, TestProviders.MAX_DIGITS]
    public static final int MAX_DIGITS = 100;
    public static Stream<Integer> invalidRandomProvider() {
        return Stream.concat(invalidPositionProvider(), IntStream.range(TestProviders.MAX_DIGITS + 1, MAX_DIGITS).boxed());
    }
}
