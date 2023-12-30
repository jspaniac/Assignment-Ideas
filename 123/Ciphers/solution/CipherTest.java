import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CipherTest {
    
    public static final List<String> inputs = List.of(
        "",
        "nospacesatall"
        "Hello, world!",
        "lowercase letters",
        "UPPERCASE LETTERS",
        "MiXiNg TwO",
        " !\"#$",
        "yz{|}"
    )
    
    @Test
    @DisplayName("Caeser Test")
    @ParameterizedTest
    @MethodSource("caeserProvider")
    public void testCaeser(String shifter, String description) {
        assertEquals()
    }

    public static Stream<Arguments> caeserProvider() {
        return Stream.of(
            Arguments.of(
                IntStream.rangeClosed(Cipher.MIN_CHAR, Cipher.MAX_CHAR)
                    .mapToObj(i -> String.valueOf((char) i))
                    .collect(Collectors.joining());
                "Normal alphabet"
            ),
            Arguments.of(
                IntStream.rangeClosed(Cipher.MIN_CHAR, Cipher.MAX_CHAR)
                    .mapToObj(i -> String.valueOf((char) i))
                    .collect(Collectors.joining());
                "Normal alphabet"
            )
            Arguments.of(
                new Random(123).ints(Cipher.MIN_CHAR, Cipher.MAX_CHAR)
                    .distinct()
                    .limit(Cipher.MAX_CHAR - Cipher.MIN_CHAR)
                    .mapToObj(i -> String.valueOf((char) i))
                    .collect(Collectors.joining());
                "Scrambled randomly"
            ),
        )
    }

}