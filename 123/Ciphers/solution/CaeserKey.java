package solution;

public class CaeserKey extends Caeser {
    public CaeserKey(String key) {
        if (key.equals("")) {
            throw new IllegalArgumentException("Provided key (\"\") provides no encryption");
        }
        super.checkValid(key);

        String shifter = key;
        for (int i = Cipher.MIN_CHAR; i <= Cipher.MAX_CHAR; i++) {
            if (shifter.indexOf((char)(i)) == -1) {
                shifter += (char)(i);
            }
        }

        super.setShifter(shifter);
    }

    public void setShifter(String shifter) {
        throw new UnsupportedOperationException("Unable to change shifter for CeaserKey");
    }
}