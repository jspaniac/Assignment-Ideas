import java.util.stream.Stream;

public class ExceptionProviders {
    // Caeser.java - Invalid shifter strings with explanation
    public static Stream<CipherTest.Tuple<String, String>> invalidShifterProvider() {
        return Stream.of(
            new CipherTest.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "No encryption"),
            new CipherTest.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ", "Too few characters"),
            new CipherTest.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} ", "Too many characters"),
            new CipherTest.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "Duplicate characters"),
            new CipherTest.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MAX_CHAR + 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside upper range"),
            new CipherTest.Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MIN_CHAR - 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside lower range")
        );
    }

    // CaeserShift.java - The number of multiples of (Cipher.MAX_VALUE - Cipher.MIN_VALUE) to test shifting by
    public static final int NUM_MULTIPLES = 100;

    // CaeserKey.java - Invalid keys with explanations
    public static Stream<CipherTest.Tuple<String, String>> invalidKeyProvider() {
        return Stream.of(
            new CipherTest.Tuple<>("", "No characters provided"),
            new CipherTest.Tuple<>("aa", "Duplicate characters"),
            new CipherTest.Tuple<>(" ", "Up to Cipher.MIN_CHAR"),
            new CipherTest.Tuple<>(" !", "Up to Cipher.MIN_CHAR + 1"),
            new CipherTest.Tuple<>(" !\"", "Up to Cipher.MIN_CHAR + 2"),
            new CipherTest.Tuple<>(" !\"#", "Up to Cipher.MIN_CHAR + 3")
        );
    }
}
