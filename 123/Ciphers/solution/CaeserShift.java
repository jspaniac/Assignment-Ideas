package solution;

public class CaeserShift extends Caeser {
    private int shift;
    public CaeserShift(int shift) {
        this.shift = shift;
        String shifter = "";
        for (int i = 0; i < Cipher.TOTAL_CHARS; i++) {
            int currShift =  (i + shift) % Cipher.TOTAL_CHARS;
            shifter += (char)((currShift < 0 ? Cipher.MAX_CHAR + 1 : Cipher.MIN_CHAR) + currShift);
        }
        super.setShifter(shifter);
    }

    public void setShifter(String shifter) {
        throw new UnsupportedOperationException("Unable to change shifter for CeaserShift");
    }

    @Override
    public String toString() {
        return String.format("CaeserShift with shift: %d", this.shift);
    }
}