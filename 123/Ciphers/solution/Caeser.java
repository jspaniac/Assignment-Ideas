package solution;

public class Caeser extends Cipher {
    private String shifter;
    
    public Caeser() {
        this.shifter = null;
    }

    public Caeser(String shifter) {
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
            if (i != 0 && shifter.charAt(i - 1) > c) {
                found = true;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("No encryption occurs with this shifter");
        }
    }

    public void setShifter(String shifter) {
        if (shifter.length() != Cipher.TOTAL_CHARS) {
            throw new IllegalArgumentException("Shifter string is incorrect length");
        }
        checkValid(shifter);
        this.shifter = shifter;
    }

    public String handleInput(String input, boolean encode) {
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
}