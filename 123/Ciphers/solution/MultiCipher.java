package solution;

import java.util.*;

public class MultiCipher extends Cipher {
    private List<Cipher> ciphers;
    public MultiCipher(List<Cipher> ciphers) {
        if (ciphers == null || ciphers.isEmpty()) {
            throw new IllegalArgumentException("Empty list of ciphers provides no encryption");
        }
        this.ciphers = new ArrayList<>(ciphers);
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
        int start = encode ? 0 : -(ciphers.size() - 1);
        int end = encode ? ciphers.size() - 1 : 0;
        for (int i = start; i <= end; i++) {
            System.out.println(i);
            input = encode ? ciphers.get(Math.abs(i)).encrypt(input) : 
                             ciphers.get(Math.abs(i)).decrypt(input);
        }
        return input;
    }

    @Override
    public String toString() {
        return ciphers.toString().substring(1, ciphers.toString().length() - 1);
    }
}