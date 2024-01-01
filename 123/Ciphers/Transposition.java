public class Transposition extends Cipher {
    private int width;
    public Transposition(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width for transposition cipher must be positive");
        }
        if (width == 1) {
            throw new IllegalArgumentException("Width for transposition (1) provides no encryption");
        }
        this.width = width;
    }

    private char[][] toMatrix(String input, boolean row) {
        int height = (int) Math.ceil(input.length() / (double) width);
        char[][] matrix = new char[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int index = row ? width * i + j : height * j + i;
                matrix[i][j] = index < input.length() ? input.charAt(index) :
                                                        (char)(Cipher.MAX_CHAR + 1);
            }
        }
        return matrix;
    }

    private String traverse(char[][] matrix, boolean row) {
        int height = matrix.length;
        char[] ret = new char[height * width];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int index = row ? width * i + j : height * j + i;
                ret[index] = matrix[i][j];
            }
        }
        return new String(ret);
    }

    public String handleInput(String input, boolean encode) {
        if (!encode && input.length() % width != 0) {
            throw new IllegalArgumentException("Input has invalid length to decode");
        }
        char[][] matrix = toMatrix(input, encode);
        String result = traverse(matrix, !encode);
        int lastIndex = result.indexOf((char)(Cipher.MAX_CHAR + 1));
        return (!encode && lastIndex != -1) ? result.substring(0, lastIndex) : result;
    }

    @Override
    public String toString() {
        return String.format("Transposition with width: %d", this.width);
    }
}