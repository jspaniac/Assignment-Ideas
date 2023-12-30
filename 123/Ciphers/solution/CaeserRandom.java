import java.util.*;

public class CaeserRandom extends Caeser {
    public static final Random RAND = new Random();
    public static final int MAX_DIGITS = (int)(Math.floor(Math.log10(Integer.MAX_VALUE)));
    
    private int digits;

    public CaeserRandom(int digits) {
        if (digits <= 0 || digits > MAX_DIGITS) {
            throw new IllegalArgumentException("Digits value < 0 or > " + MAX_DIGITS);
        }
        this.digits = digits;
    }

    private static String getShifter(int seed) {
        List<Character> shifterList = new ArrayList<>();
        for (int i = Cipher.MIN_CHAR; i <= Cipher.MAX_CHAR; i++) {
            shifterList.add((char)(i));
        }
        Collections.shuffle(shifterList, new Random(seed));
        String shifter = "";
        for (Character c : shifterList) {
            shifter += c;
        }
        return shifter;
    }

    private static String toDigits(int digits, int val) {
        return String.format("%0" + digits + "d", val);
    }

    public String handleInput(String input, boolean encode) {
        int seed = encode ? RAND.nextInt((int)(Math.pow(10, digits))) : Integer.parseInt(input.substring(0, digits));
        super.setShifter(getShifter(seed));
        return encode ? toDigits(digits, seed) + super.handleInput(input, encode) :
                        super.handleInput(input.substring(digits), encode);
    }
}