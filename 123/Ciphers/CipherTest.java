import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

public class CipherTest {
    private static class Tuple {
        public String one;
        public String two;

        public Tuple(String one, String two) {
            this.one = one; this.two = two;
        }

        @Override
        public String toString() {
            return String.format("(%s, %s)", one, two);
        }
    }

    // List of encode / decode inputs with explanations
    public static final List<Tuple> inputs = List.of(
        new Tuple("", "No input"),
        new Tuple("abcdefghijklmnopqrstuvwxyz", "alphabetic lowercase"),
        new Tuple("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "alphabetic uppercase"),
        new Tuple("MiXiNgTwO", "Mixing uppercase and lowercase"),
        new Tuple("0123456789", "Numbers incrementing"),
        new Tuple("9876543210", "Numbers decrementing"),
        new Tuple(" !\"#$", "Characters near Cipher.MIN_CHAR"),
        new Tuple("yz{|}", "Characters near Cipher.MAX_CHAR")
    );

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
    public void testCaeserEncode(Tuple shifter) {
        solution.Cipher solution = new solution.Caeser(shifter.one);
        Cipher student = new Caeser(shifter.one);
       
        for (int i = 0; i <= 1; i++) {
            boolean encode = i == 0;
            for (Tuple input : inputs) {
                String expected = solution.handleInput(input.one, encode);
                String actual = student.handleInput(input.one, encode);
                assertEquals(expected, actual,
                            String.format("%s failed with shifter [%s] and input %s", 
                                        (encode ? "Encode" : "Decode"), shifter.two, input
                            )
                );
            }
        }
    }

    public static Stream<Tuple> shifterProvider() {
        return Stream.of(
            new Tuple(
                " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`bacdefghijklmnopqrstuvwxyz{|}",
                "Swapping a<->b"
            ),
            new Tuple(
                " !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`ABCDEFGHIJKLMNOPQRSTUVWXYZ{|}",
                "Swapping upper and lowercase letters"
            ),
            new Tuple(
                " !\"#$%&'()*+,-./9876543210:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}",
                "Decrementing numbers"
            ),
            new Tuple(
                "j+]g,xH0 d)(*io#O!&?}1;|YR{L/^CZcV4TsAEf'e`_=h<%[w\\5Wv-Mt\"nF>a2X6:BuIN3KPQ8.$zGySl79kq@mrUJpDb",
                "Scrambled randomly"
            )
        );
    }

    @DisplayName("Exceptions test")
    @Test
    @Tag("Score:0")
    public void testExceptions() {
        testCaeserExceptions();
        // testCaeserShiftExceptions();
        // testCaeserKeyExceptions();
    }

    public void testCaeserExceptions() {
        invalidShifterProvider().forEach(tuple -> {
            assertThrows(IllegalArgumentException.class, () -> {
                new Caeser(tuple.one);
            }, String.format("Appropriate exception not thrown for shifter type [%s] in Caeser.java constructor", tuple.two));

            assertThrows(IllegalArgumentException.class, () -> {
                Caeser c = new Caeser();
                c.setShifter(tuple.one);
            }, String.format("Appropriate exception not thrown for shifter type [%s] in Caeser.java setShifter constructor", tuple.two));
        });

        assertThrows(IllegalStateException.class, () -> {
            Caeser c = new Caeser();
            c.handleInput("a", true);
        }, "Appropriate exception not thrown for handling input without setting shifter in Caeser.java");
    }

    public static Stream<Tuple> invalidShifterProvider() {
        return Stream.of(
            new Tuple(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "No encryption"),
            new Tuple(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ", "Too few characters"),
            new Tuple(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} ", "Too many characters"),
            new Tuple(" !\"#$%&'()*+,-./0123456789:;<=>?@abcdefghijklmnopqrstuvwxyz[\\]^_`abcdefghijklmnopqrstuvwxyz{|}", "Duplicate characters"),
            new Tuple(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MAX_CHAR + 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside upper range"),
            new Tuple(" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + (char)(Cipher.MIN_CHAR - 1) + "[\\]^_`abcdefghijklmnopqrstuvwxyz{|", "Outside lower range")
        );
    }
}