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
    public void testCaesarBase(Tuple<String, String> shifter) {
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
        Tuple<List<solution.Cipher>, List<Cipher>> converted = CipherTest.cipherListConverter(ciphers);
        testCiphers(new solution.MultiCipher(converted.one), new MultiCipher(converted.two), "");
    }

    @DisplayName("Concealment Test All")
    @Test
    @Tag("Score:0")
    public void testConcealment() {
        // Base
        TestProviders.concealmentProvider().forEach(position -> {
            for (Tuple<String, String> input : TestProviders.INPUTS) {
                Cipher c = new Concealment(position);
                String encoded = c.handleInput(input.one, true);
                assertTrue(encoded.length() / (position + 1) == input.one.length(),
                           String.format("Encoded string has incorrect length for input %s and filler [%d], expected in range: [%d < length < %d], actual: %d. Result: [%s]",
                                         input.toString(), position, input.one.length() * (position + 1), input.one.length() * (position + 2), encoded.length(), encoded));
                for (int i = position; i < encoded.length(); i += position + 1) {
                    assertEquals(input.one.charAt(i / (position + 1)), encoded.charAt(i),
                                 String.format("Encoded string has incorrect character at index [%d] for input %s and filler [%d]. Result: [%s]",
                                               i, input.toString(), position, encoded));
                }
                assertEquals(input.one, c.handleInput(encoded, false), 
                             String.format("Decoding failed for encoded input [%s] and filler [%d]. Original input %s",
                                           encoded, position, input.toString()));
            }
        });

        // Exceptions
        ExceptionProviders.invalidPositionProvider().forEach(position -> {
            assertThrows(IllegalArgumentException.class, () -> new Concealment(position),
                         String.format("Appropriate exception not thrown for filler value [%d] in Concealment.java constructor",
                                       position));
        });
    }

    @DisplayName("Vigenere Test All")
    @Test
    @Tag("Score:0")
    public void testVigenere() {
        // Base
        TestProviders.vigenereProvider().forEach(key -> {
            testCiphers(new solution.Vigenere(key), new Vigenere(key), "");
        });

        // Exceptions
        ExceptionProviders.invalidVigenereProvider().forEach(key -> {
            assertThrows(IllegalArgumentException.class, () -> new Vigenere(key),
                         String.format("Appropriate exception not thrown for non-encrypting key [%s] in Vigenere.java constructor",
                                       key));
        });
    }

    @DisplayName("Transposition Test All")
    @Test
    @Tag("Score:0")
    public void testTransposition() {
        // Base
        TestProviders.transpositionProvider().forEach(width -> {
            testCiphers(new solution.Transposition(width), new Transposition(width), "");
        });

        // Exceptions
        ExceptionProviders.invalidTranspositionProvider().forEach(width -> {
            assertThrows(IllegalArgumentException.class, () -> new Transposition(width),
                         String.format("Appropriate exception not thrown for width value [%d] in Tranpsoition.java constructor",
                                       width));
        });
    }

    @DisplayName("CaesarRandom Test All")
    @Test
    @Tag("Score:0")
    public void testCaesarRandom() {
        // Base
        TestProviders.randomProvider().forEach(digits -> {
            Cipher c = new CaesarRandom(digits);
            for (Tuple<String, String> input : TestProviders.INPUTS) {
                String encoded = c.handleInput(input.one, true);
                String decoded = c.handleInput(encoded, false);
                assertEquals(input.one, decoded, String.format("Decoded string mismatches original with digits [%d]",
                                                               digits));
            }
        });

        // Exceptions
        ExceptionProviders.invalidRandomProvider().forEach(digits -> {
            assertThrows(IllegalArgumentException.class, () -> new CaesarRandom(digits),
                         String.format("Appropriate exception not thrown for digits value [%d] in CaesarRandom.java constructor",
                                       digits));
        });
    }


    public static Tuple<List<solution.Cipher>, List<Cipher>> cipherListConverter(List<String> input) {
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
        return new Tuple<>(solutionCiphers, studentCiphers);
    }

    public void testCiphers(solution.Cipher solution, Cipher student, String additionalDescription) {
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