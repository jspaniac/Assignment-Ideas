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
    
    @DisplayName("Caesar Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("TestProviders#shifterProvider")
    public void testCaesarBase(TestProviders.Tuple<String, String> shifter) {
        testCiphers(new solution.Caesar(shifter.one), new Caesar(shifter.one), shifter.toString());
    }

    @DisplayName("CaesarShift Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("TestProviders#shiftProvider")
    public void testCaesarShift(int shift) {
        testCiphers(new solution.CaesarShift(shift), new CaesarShift(shift), "");
    }

    @DisplayName("CaesarKey Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("TestProviders#keyProvider")
    public void testCaesarKey(String key) {
        testCiphers(new solution.CaesarKey(key), new CaesarKey(key), "");
    }

    @DisplayName("MultiCipher Test Encode/Decode")
    @ParameterizedTest
    @Tag("Score:0")
    @MethodSource("TestProviders#cipherListProvider")
    public void testMultiCipher(List<String> ciphers) {
        TestProviders.Tuple<List<solution.Cipher>, List<Cipher>> converted = CipherTest.cipherListConverter(ciphers);
        testCiphers(new solution.MultiCipher(converted.one), new MultiCipher(converted.two), "");
    }

    public static TestProviders.Tuple<List<solution.Cipher>, List<Cipher>> cipherListConverter(List<String> input) {
        List<Cipher> studentCiphers = new ArrayList<>();
        List<solution.Cipher> solutionCiphers = new ArrayList<>();
        for (String cipher : input) {
            if (cipher.charAt(0) == 'c') {
                studentCiphers.add(new Caesar(cipher.substring(1)));
                solutionCiphers.add(new solution.Caesar(cipher.substring(1)));
            } else if (cipher.charAt(0) == 's') {
                studentCiphers.add(new CaesarShift(Integer.valueOf(cipher.substring(1))));
                solutionCiphers.add(new solution.CaesarShift(Integer.valueOf(cipher.substring(1))));
            } else if (cipher.charAt(0) == 'k') {
                studentCiphers.add(new CaesarKey(cipher.substring(1)));
                solutionCiphers.add(new solution.CaesarKey(cipher.substring(1)));
            }
        }
        return new TestProviders.Tuple<>(solutionCiphers, studentCiphers);
    }

    public void testCiphers(solution.Cipher solution, Cipher student, String additionalDescription) {
        for (int i = 0; i < 2; i++) {
            boolean encode = i == 0;
            for (TestProviders.Tuple<String, String> input : TestProviders.INPUTS) {
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

    @DisplayName("Base Exceptions Test")
    @Test
    @Tag("Score:0")
    public void testExceptions() {
        testCaesarBaseExceptions();
        testCaesarShiftExceptions();
        testCaesarKeyExceptions();
        testMultiExceptions();
    }

    public void testCaesarBaseExceptions() {
        ExceptionProviders.invalidShifterProvider().forEach(tuple -> {
            assertThrows(IllegalArgumentException.class, () -> {
                new Caesar(tuple.one);
            }, String.format("Appropriate exception not thrown for shifter type [%s] in Caesar.java constructor", tuple.two));

            assertThrows(IllegalArgumentException.class, () -> {
                Caesar c = new Caesar();
                c.setShifter(tuple.one);
            }, String.format("Appropriate exception not thrown for shifter type [%s] in Caesar.java setShifter", tuple.two));
        });

        assertThrows(IllegalStateException.class, () -> {
            Caesar c = new Caesar();
            c.handleInput("a", true);
        }, "Appropriate exception not thrown for handling input without setting shifter in Caesar.java");
    }

    public void testCaesarShiftExceptions() {
        // TODO: If negative shift values are considered invalid, update that here!
        for (int i = 0; i < ExceptionProviders.NUM_MULTIPLES; i++) {
            final Integer ai = Integer.valueOf(i);
            assertThrows(IllegalArgumentException.class, () -> {
                new CaesarShift((Cipher.MAX_CHAR - Cipher.MIN_CHAR + 1) * ai);
            }, String.format("Appropriate exception not thrown for shift value that's a multiple of %d [%d] in CaesarShift.java constructor",
                             Cipher.MAX_CHAR - Cipher.MIN_CHAR, (Cipher.MAX_CHAR - Cipher.MIN_CHAR + 1) * ai));
        }
    }

    public void testCaesarKeyExceptions() {
        ExceptionProviders.invalidKeyProvider().forEach(tuple -> {
            assertThrows(IllegalArgumentException.class, () -> {
                new CaesarKey(tuple.one);
            }, String.format("Appropriate exception not thrown for no-encryption key [%s] in CaesarKey.java constructor", tuple.two));
        });
    }

    public void testMultiExceptions() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MultiCipher(new ArrayList<>());
        }, "Appropriate exception not thrown for empty encryption list");
    }
}