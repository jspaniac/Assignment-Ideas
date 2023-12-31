package solution;

public class CaesarKey extends Caesar {
    private String key;

    public CaesarKey(String key) {
        //super.checkValid(key);
        this.key = key;
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

    @Override
    public String toString() {
        return String.format("CaesarKey with key: %s", this.key);
    }
}