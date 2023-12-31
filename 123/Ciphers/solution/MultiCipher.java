package solution;

import java.util.*;

public class MultiCipher extends Cipher {
    private List<Cipher> ciphers;
    public MultiCipher(List<Cipher> ciphers) {
        if (ciphers.isEmpty()) {
            throw new IllegalArgumentException("Empty list of ciphers provides no encryption");
        }
        this.ciphers = new ArrayList<>(ciphers);
    }

    public String handleInput(String input, boolean encode) {
        int start = encode ? 0 : -(ciphers.size() - 1);
        int end = encode ? ciphers.size() - 1 : 0;
        for (int i = start; i <= end; i++) {
            input = ciphers.get(Math.abs(i)).handleInput(input, encode);
        }
        return input;
    }
}