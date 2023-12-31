package solution;

import java.util.*;

public class Concealment extends Cipher {
    public static final Random RAND = new Random();
    
    private int position;

    public Concealment(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Position must not be negative");
        }
        if (position == 0) {
            throw new IllegalArgumentException("Position for concealment (0) provides no encryption");
        }
        this.position = position;
    }

    @Override
    public String encrypt(String input) {
        return handleInput(input, true);
    }

    @Override
    public String decrypt(String input) {
        return handleInput(input, false);
    }

    private String handleInput(String input, boolean encode) {
        String ret = "";
        for (int i = 0; i < input.length(); i++) {
            if (encode) {
                for (int j = 0; j < position; j++) {
                    ret += (char)(Cipher.MIN_CHAR + RAND.nextInt(Cipher.TOTAL_CHARS + 1));
                }
            } else {
                i += position;
            }

            if (i < input.length()) {
                ret += input.charAt(i);
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return String.format("Concealment with position: %d", this.position);
    }
}