import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

public class CipherTest {

    // TODO: Change if wanting to add more inputs to encode / decode
    public static final List<Tuple<String, String>> inputs = List.of(
        new Tuple<>("", "No input"),
        new Tuple<>("abcdefghijklmnopqrstuvwxyz", "alphabetic lowercase"),
        new Tuple<>("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "alphabetic uppercase"),
        new Tuple<>("MiXiNgTwO", "Mixing uppercase and lowercase"),
        new Tuple<>("0123456789", "Numbers incrementing"),
        new Tuple<>("9876543210", "Numbers decrementing"),
        new Tuple<>(" !\"#$", "Characters near Cipher.MIN_CHAR"),
        new Tuple<>("yz{|}", "Characters near Cipher.MAX_CHAR")
    );

    // Base test streams:
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
        return Stream.of(1, 5, 10, -1, -5, -10);
    }

    // CaeserKey.java - Key values provided to constructor
    public static Stream<String> keyProvider() {
        return Stream.of("BAG", "bag", "cse123", "" + (char)(Cipher.MAX_CHAR));
    }

    // MultiCipher.java - Strings that are converted into ciphers. c=Caeser, k=CaeserKey, s=CaeserShift
    public static Stream<List<String>> cipherListProvider() {
        return Stream.of(
            List.of("c !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`bacdefghijklmnopqrstuvwxyz{|}"),
            List.of("kcse123"),
            List.of("s10"),
            List.of("c !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`bacdefghijklmnopqrstuvwxyz{|}", "kcse123", "s10")
        );
    }

    // Exception test streams:
    // Caeser.java - Invalid shifter strings with explanation
    public static Stream<Tuple<String, String>> invalidShifterProvider() {
        return Stream.of(
            new Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "No encryption"),
            new Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ", "Too few characters"),
            new Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} ", "Too many characters"),
            new Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "Duplicate characters"),
            new Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MAX_CHAR + 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside upper range"),
            new Tuple<>(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MIN_CHAR - 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside lower range")
        );
    }

    // CaeserShift.java - The number of multiples of (Cipher.MAX_VALUE - Cipher.MIN_VALUE) to test shifting by
    public static final int NUM_MULTIPLES = 100;

    // CaeserKey.java - Invalid keys with explanations
    public static Stream<Tuple<String, String>> invalidKeyProvider() {
        return Stream.of(
            new Tuple<>("", "No characters provided"),
            new Tuple<>("aa", "Duplicate characters"),
            new Tuple<>(" ", "Up to Cipher.MIN_CHAR"),
            new Tuple<>(" !", "Up to Cipher.MIN_CHAR + 1"),
            new Tuple<>(" !\"", "Up to Cipher.MIN_CHAR + 2"),
            new Tuple<>(" !\"#", "Up to Cipher.MIN_CHAR + 3")
        );
    }

    // MultiCipher.java - Just an empty list lol

    @BeforeEach
    public void setUp() {
        assertEquals(Cipher.MIN_CHAR, solution.Cipher.MIN_CHAR,
                     "Cipher.MIN_CHAR has been modified! Make sure to revert it to " + solution.Cipher.MIN_CHAR);
        assertEquals(Cipher.MAX_CHAR, solution.Cipher.MAX_CHAR,
                     "Cipher.MAX_CHAR has been modified! Make sure to revert it to " + solution.Cipher.MAX_CHAR);
    }
    
    @DisplayName("Caeser Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("shifterProvider")
    public void testCaeserBase(Tuple<String, String> shifter) {
        testCiphers(new solution.Caeser(shifter.one), new Caeser(shifter.one), shifter.toString());
    }

    @DisplayName("CaeserShift Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("shiftProvider")
    public void testCaeserShift(int shift) {
        testCiphers(new solution.CaeserShift(shift), new CaeserShift(shift), "");
    }

    @DisplayName("CaeserKey Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("keyProvider")
    public void testCaeserKey(String key) {
        testCiphers(new solution.CaeserKey(key), new CaeserKey(key), "");
    }

    @DisplayName("MultiCipher Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("cipherListProvider")
    public void testMultiCipher(List<String> ciphers) {
        Tuple<List<Cipher>, List<solution.Cipher>> converted = cipherListConverter(ciphers);
        testCiphers(new solution.MultiCipher(converted.two), new MultiCipher(converted.one), "");
    }

    public Tuple<List<Cipher>, List<solution.Cipher>> cipherListConverter(List<String> input) {
        List<Cipher> studentCiphers = new ArrayList<>();
        List<solution.Cipher> solutionCiphers = new ArrayList<>();
        for (String cipher : input) {
            if (cipher.charAt(0) == 'c') {
                studentCiphers.add(new Caeser(cipher.substring(1)));
                solutionCiphers.add(new solution.Caeser(cipher.substring(1)));
            } else if (cipher.charAt(0) == 's') {
                studentCiphers.add(new CaeserShift(Integer.valueOf(cipher.substring(1))));
                solutionCiphers.add(new solution.CaeserShift(Integer.valueOf(cipher.substring(1))));
            } else if (cipher.charAt(0) == 'k') {
                studentCiphers.add(new CaeserKey(cipher.substring(1)));
                solutionCiphers.add(new solution.CaeserKey(cipher.substring(1)));
            }
        }
        return new Tuple<>(studentCiphers, solutionCiphers);
    }

    public void testCiphers(solution.Cipher solution, Cipher student, String additionalDescription) {
        for (int i = 0; i <= 1; i++) {
            boolean encode = i == 0;
            for (Tuple<String, String> input : inputs) {
                String expected = solution.handleInput(input.one, encode);
                String actual = student.handleInput(input.one, encode);
                assertEquals(expected, actual,
                            String.format("%s failed with [%s] and input %s", 
                                          (encode ? "Encode" : "Decode"),
                                          (additionalDescription.isEmpty() ? solution.toString() : additionalDescription),
                                          input.toString())
                );
            }
        }
    }

    @DisplayName("Exceptions test")
    @Test
    @Tag("Score:0")
    public void testExceptions() {
        testCaeserBaseExceptions();
        testCaeserShiftExceptions();
        testCaeserKeyExceptions();
        testMultiExceptions();
    }

    public void testCaeserBaseExceptions() {
        invalidShifterProvider().forEach(tuple -> {
            assertThrows(IllegalArgumentException.class, () -> {
                new Caeser(tuple.one);
            }, String.format("Appropriate exception not thrown for shifter type [%s] in Caeser.java constructor", tuple.two));

            assertThrows(IllegalArgumentException.class, () -> {
                Caeser c = new Caeser();
                c.setShifter(tuple.one);
            }, String.format("Appropriate exception not thrown for shifter type [%s] in Caeser.java setShifter", tuple.two));
        });

        assertThrows(IllegalStateException.class, () -> {
            Caeser c = new Caeser();
            c.handleInput("a", true);
        }, "Appropriate exception not thrown for handling input without setting shifter in Caeser.java");
    }

    public void testCaeserShiftExceptions() {
        // TODO: If negative shift values are considered invalid, update that here!
        for (int i = 0; i < NUM_MULTIPLES; i++) {
            final Integer ai = Integer.valueOf(i);
            assertThrows(IllegalArgumentException.class, () -> {
                new CaeserShift((Cipher.MAX_CHAR - Cipher.MIN_CHAR + 1) * ai);
            }, String.format("Appropriate exception not thrown for shift value that's a multiple of %d in CaeserShift.java constructor", Cipher.MAX_CHAR - Cipher.MIN_CHAR));
        }
    }

    public void testCaeserKeyExceptions() {
        invalidKeyProvider().forEach(tuple -> {
            assertThrows(IllegalArgumentException.class, () -> {
                new CaeserKey(tuple.one);
            }, String.format("Appropriate exception not thrown for key [%s] in CaeserKey.java constructor", tuple.two));
        });
    }

    public void testMultiExceptions() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MultiCipher(new ArrayList<>());
        });
    }

    private static class Tuple<A, B> {
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