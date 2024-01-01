import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExtensionTest {

    @BeforeEach
    public void setUp() {
        assertEquals(Cipher.MIN_CHAR, solution.Cipher.MIN_CHAR,
                     "Cipher.MIN_CHAR has been modified! Make sure to revert it to " + solution.Cipher.MIN_CHAR);
        assertEquals(Cipher.MAX_CHAR, solution.Cipher.MAX_CHAR,
                     "Cipher.MAX_CHAR has been modified! Make sure to revert it to " + solution.Cipher.MAX_CHAR);
    }

    @DisplayName("Concealment Test All")
    @Test
    @Tag("Score:0")
    public void testConcealment() {
        // Base
        TestProviders.concealmentProvider().forEach(position -> {
            Cipher c = new Concealment(position);
            for (TestProviders.Tuple<String, String> input : TestProviders.INPUTS) {
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
            Cipher c = new Transposition(width);
            solution.Cipher sc = new solution.Transposition(width);
            for (TestProviders.Tuple<String, String> input : TestProviders.INPUTS) {
                String encoded = c.handleInput(input.one, true);
                assertEquals(sc.handleInput(input.one, true), encoded,
                             String.format("Input incorrectly encoded. Originally %s, Encoded [%s]",
                                           input.toString(), encoded));
                String decoded = c.handleInput(encoded, false);
                assertEquals(decoded, input.one,
                             String.format("Encoded input incorrect decoded. Originally %s, Encoded [%s], Decoded [%s]",
                                           input.toString(), encoded, decoded));
            }
        });

        // Exceptions
        ExceptionProviders.invalidTranspositionProvider().forEach(width -> {
            assertThrows(IllegalArgumentException.class, () -> new Transposition(width),
                         String.format("Appropriate exception not thrown for width value [%d] in Transposition.java constructor",
                                       width));
        });
        Cipher c = new Transposition(ExceptionProviders.INVALID_WIDTH);
        ExceptionProviders.invalidTranspositionDecryptProvider().forEach(encoded -> {
            assertThrows(IllegalArgumentException.class, () -> c.handleInput(encoded, false),
                         "Appropriate exception not thrown when decoding string with length not a multiple of width in Transposition.java handleInput");
        });
    }

    @DisplayName("CaesarRandom Test All")
    @Test
    @Tag("Score:0")
    public void testCaesarRandom() {
        // Base
        TestProviders.randomProvider().forEach(digits -> {
            Cipher c = new CaesarRandom(digits);
            for (TestProviders.Tuple<String, String> input : TestProviders.INPUTS) {
                String encoded = c.handleInput(input.one, true);
                String decoded = c.handleInput(encoded, false);
                assertEquals(input.one, decoded, String.format("Decoded string mismatches original with digits [%d]. Original %s Encoded [%s], Decoded [%s]",
                                                               digits, input.toString(), encoded, decoded));
            }
        });

        // Exceptions
        ExceptionProviders.invalidRandomProvider().forEach(digits -> {
            assertThrows(IllegalArgumentException.class, () -> new CaesarRandom(digits),
                         String.format("Appropriate exception not thrown for digits value [%d] in CaesarRandom.java constructor",
                                       digits));
        });
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
}