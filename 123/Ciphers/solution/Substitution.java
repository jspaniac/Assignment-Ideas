package solution;

public class Substitution extends Cipher {
    private String shifter;
    
    public Substitution() {
        this.shifter = null;
    }

    public Substitution(String shifter) {
        setShifter(shifter);
    }

    public static void checkValid(String shifter) {
        boolean found = false;
        for (int i = 0; i < shifter.length(); i++) {
            char c = shifter.charAt(i);
            if (c < Cipher.MIN_CHAR || c > Cipher.MAX_CHAR) {
                throw new IllegalArgumentException("Character " + c + " is outside the range of valid characters");
            }
            if (shifter.indexOf(c) != i) {
                throw new IllegalArgumentException(shifter + " contains duplicate character " + c);
            }
            if (shifter.charAt(i) != Cipher.MIN_CHAR + i) {
                found = true;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("No encryption occurs with this shifter: " + shifter);
        }
    }

    public void setShifter(String shifter) {
        if (shifter.length() != Cipher.TOTAL_CHARS) {
            throw new IllegalArgumentException("Shifter string is incorrect length");
        }
        checkValid(shifter);
        this.shifter = shifter;
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
        if (this.shifter == null) {
            throw new IllegalStateException("Shifter never set after empty construction");
        }
        String ret = "";
        for (int i = 0; i < input.length(); i++) {
            ret += encode ? shifter.charAt((int)(input.charAt(i)) - Cipher.MIN_CHAR) :
                            (char)(shifter.indexOf(input.charAt(i)) + Cipher.MIN_CHAR);
        }
        return ret;
    }

    @Override
    public String toString() {
        return String.format("Caesar with shifter: %s", this.shifter);
    }
}