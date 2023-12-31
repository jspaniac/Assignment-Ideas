import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class CipherTest {

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
    @MethodSource("TestProviders#shifterProvider")
    public void testCaeserBase(Tuple<String, String> shifter) {
        CipherTest.testCiphers(new solution.Caeser(shifter.one), new Caeser(shifter.one), shifter.toString());
    }

    @DisplayName("CaeserShift Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("TestProviders#shiftProvider")
    public void testCaeserShift(int shift) {
        CipherTest.testCiphers(new solution.CaeserShift(shift), new CaeserShift(shift), "");
    }

    @DisplayName("CaeserKey Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("TestProviders#keyProvider")
    public void testCaeserKey(String key) {
        CipherTest.testCiphers(new solution.CaeserKey(key), new CaeserKey(key), "");
    }

    @DisplayName("MultiCipher Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("TestProviders#cipherListProvider")
    public void testMultiCipher(List<String> ciphers) {
        Tuple<List<solution.Cipher>, List<Cipher>> converted = CipherTest.cipherListConverter(ciphers);
        CipherTest.testCiphers(new solution.MultiCipher(converted.one), new MultiCipher(converted.two), "");
    }

    public static Tuple<List<solution.Cipher>, List<Cipher>> cipherListConverter(List<String> input) {
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
        return new Tuple<>(solutionCiphers, studentCiphers);
    }

    public static void testCiphers(solution.Cipher solution, Cipher student, String additionalDescription) {
        for (int i = 0; i < 2; i++) {
            boolean encode = i == 0;
            for (Tuple<String, String> input : TestProviders.INPUTS) {
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
        ExceptionProviders.invalidShifterProvider().forEach(tuple -> {
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
        for (int i = 0; i < ExceptionProviders.NUM_MULTIPLES; i++) {
            final Integer ai = Integer.valueOf(i);
            assertThrows(IllegalArgumentException.class, () -> {
                new CaeserShift((Cipher.MAX_CHAR - Cipher.MIN_CHAR + 1) * ai);
            }, String.format("Appropriate exception not thrown for shift value that's a multiple of %d in CaeserShift.java constructor", Cipher.MAX_CHAR - Cipher.MIN_CHAR));
        }
    }

    public void testCaeserKeyExceptions() {
        ExceptionProviders.invalidKeyProvider().forEach(tuple -> {
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