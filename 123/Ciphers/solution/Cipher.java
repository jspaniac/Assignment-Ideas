package solution;

import java.util.*;
import java.io.*;

// Represents a classical cipher that is able to encode a plaintext into a ciphertext, and
// decode a ciphertext into a plaintext. Also capable of encoding and decoding entire files

public abstract class Cipher {
    // The minimum character able to be encoded by any cipher
    public static final int MIN_CHAR = (int)(' ');
    
    // The maximum character able to be encoded by any cipher
    public static final int MAX_CHAR = (int)('}');
    
    // The total number of characters able to be encoded by any cipher (aka. the encodable range)
    public static final int TOTAL_CHARS = MAX_CHAR - MIN_CHAR + 1;

    // Pre: Throws a FileNotFoundException if a file with the provided 'fileName' doesn't exist
    // Post: Applies this Cipher's encryption scheme to the file with the given 'fileName', creating
    //       a new file to store the results.
    public void encryptFile(String fileName) throws FileNotFoundException {
        fileHelper(fileName, true, "-encoded");
    }
    
    // Pre: Throws a FileNotFoundException if a file with the provided 'fileName' doesn't exist
    // Post: Applies this Cipher's decryption scheme (reversing a single round of encryption if applied)
    //       to the file with the given 'fileName', creating a new file to store the results.
    public void decryptFile(String fileName) throws FileNotFoundException {
        fileHelper(fileName, false, "-decoded");
    }
    
    // Pre: Throws a FileNotFoundException if a file with the provided 'fileName' doesn't exist
    // Post: Reads from an input file with 'fileName', either encrypting or decrypting depending on 'encode',
    //       printing the results to a new file with 'suffix' appended to the input file's name
    private void fileHelper(String fileName, boolean encode, String suffix) throws FileNotFoundException{
        Scanner sc = new Scanner(new File(fileName));
        String out = fileName.split("\\.txt")[0] + suffix + ".txt";
        PrintStream ps = new PrintStream(out);
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            ps.println(encode ? encrypt(line) : decrypt(line));
        }
    }

    // Post: Returns the result of applying this Cipher's encryption scheme to 'input'
    public abstract String encrypt(String input);
    
    // Post: Returns the result of applying this Cipher's decryption scheme to 'input'
    public abstract String decrypt(String input);
}