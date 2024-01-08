package solution;

public class Vigenere extends Cipher {
    private String key;
    public Vigenere(String key) {
        if (key.equals("")) {
            throw new IllegalArgumentException("Provided key must not be empty");
        }
        boolean found = false;
        for (int i = 0; !found && i < key.length(); i++) {
            if (key.charAt(i) != Cipher.MIN_CHAR) {
                found = true;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Provided key must not contain all " +
                                                (char)(Cipher.MIN_CHAR) + "'s");
        }
        this.key = key;
    }

    @Override
    public String encrypt(String input) {
        return handleInput(input, true);
    }

    @Override
    public String decrypt(String input) {
        return handleInput(input, false);
    }

    public String handleInput(String input, boolean encode) {
        String ret = "";
        for (int i = 0; i < input.length(); i++) {
            int keyShift = (int)(key.charAt(i % key.length())) - Cipher.MIN_CHAR;
            int displacement = (input.charAt(i) - Cipher.MIN_CHAR) +
                               (encode ? keyShift : -keyShift);
            ret += (char)((displacement < 0 ? Cipher.MAX_CHAR + 1 : Cipher.MIN_CHAR) +
                          (displacement % Cipher.TOTAL_CHARS));
        }
        return ret;
    }

    @Override
    public String toString() {
        return String.format("Vigenere with key: %s", this.key);
    }
}