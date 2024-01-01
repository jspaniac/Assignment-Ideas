import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestProviders {
    // Inputs to encrypt and decrypt across all ciphers
    public static final List<Tuple<String, String>> INPUTS = List.of(
        new Tuple<>("", "No input"),
        new Tuple<>("abcdefghijklmnopqrstuvwxyz", "alphabetic lowercase"),
        new Tuple<>("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "alphabetic uppercase"),
        new Tuple<>("MiXiNgTwO", "Mixing uppercase and lowercase"),
        new Tuple<>("0123456789", "Numbers incrementing"),
        new Tuple<>("9876543210", "Numbers decrementing"),
        new Tuple<>(" !\"#$", "Characters near Cipher.MIN_CHAR"),
        new Tuple<>("yz{|}", "Characters near Cipher.MAX_CHAR"),
        new Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`bacdefghijklmnopqrstuvwxyz{|}", "All chars")
    );

    // Caeser.java - Shifter strings provided to constructor with explanation
    public static Stream<Tuple<String, String>> shifterProvider() {
        return Stream.of(
            new Tuple<>(
                " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`bacdefghijklmnopqrstuvwxyz{|}",
                "Swapping a<->b"
            ),
            new Tuple<>(
                " !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`ABCDEFGHIJKLMNOPQRSTUVWXYZ{|}",
                "Swapping upper and lowercase letters"
            ),
            new Tuple<>(
                " !\"#$%&'()*+,-./9876543210:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}",
                "Decrementing numbers"
            ),
            new Tuple<>(
                "j+]g,xH0 d)(*io#O!&?}1;|YR{L/^CZcV4TsAEf'e`_=h<%[w\\5Wv-Mt\"nF>a2X6:BuIN3KPQ8.$zGySl79kq@mrUJpDb",
                "Scrambled randomly"
            )
        );
    }

    // CaeserShift.java - Shift values provided to constructor
    public static Stream<Integer> shiftProvider() {
        return Stream.of(1, 5, 10, -1, -5, -10, Cipher.MAX_CHAR + 1, -Cipher.MAX_CHAR - 1);
    }

    // CaeserKey.java - Key values provided to constructor
    public static Stream<String> keyProvider() {
        return Stream.of("BAG", "bag", "cse123", "" + (char)(Cipher.MAX_CHAR));
    }

    // MultiCipher.java - String lists that are converted into multiciphers. c=Caeser, k=CaeserKey, s=CaeserShift
    public static Stream<List<String>> cipherListProvider() {
        return Stream.of(
            List.of("c !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`bacdefghijklmnopqrstuvwxyz{|}"),
            List.of("kcse123"),
            List.of("s10"),
            List.of("c !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`bacdefghijklmnopqrstuvwxyz{|}", "kcse123", "s10")
        );
    }

    // Concealment.java - Up to what filler value is tested
    public static final int MAX_CONCEALMENT = 100;
    public static Stream<Integer> concealmentProvider() {
        return IntStream.range(1, MAX_CONCEALMENT + 1).boxed();
    }

    // Vigenere.java - Key values provided to constructor
    public static Stream<String> vigenereProvider() {
        return Stream.concat(keyProvider(), List.of("vigenere", "VIGENERE", Cipher.MIN_CHAR + "" + Cipher.MAX_CHAR).stream());
    }

    // Transposition.java - Uses same values as concealment skipping the 1
    public static Stream<Integer> transpositionProvider() {
        return concealmentProvider().skip(1);
    }

    // CaesarRandom.java - Tests all possible number of digits
    public static final int MAX_DIGITS = (int)(Math.floor(Math.log10(Integer.MAX_VALUE)));
    public static Stream<Integer> randomProvider() {
        return IntStream.range(1, MAX_DIGITS + 1).boxed();
    }

    public static class Tuple<A, B> {
        public final A one;
        public final B two;

        public Tuple(A one, B two) {
            this.one = one; this.two = two;
        }

        @Override
        public String toString() {
            return String.format("(%s, %s)", two.toString(), one.toString());
        }
    }
}
