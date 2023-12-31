# 123 Encryption Schemes
___
## Background
Cryptography (not to be confused with crypto and blockchain) is a branch of Computer Science and Mathematics concerned with turning input messages (plaintexts) into encrypted ones (ciphertexts) that allow for discreet transfer past adversaries. The most modern and secure of these protocols are heavily influenced by advanced mathematical concepts such that they are proven to leak 0 information about the plaintext. As the Internet itself consists of sending messages through other devices to reach an endpoint, this feature is crucial! Without it, much of the Internet we take for granted would be impossible to implement safely (giving credit card info to retailers, authenticating senders, secure messaging, etc.) as anyone could gather anyone else's private information.

## Assignment
For this assignment, you'll be required to implement a number of [classical ciphers](https://en.wikipedia.org/wiki/Classical_cipher) making use of your knowledge of abstract classes and inheritence to reduce redundancy whenever possible. Once completed, you should be able to encode information past the point of any human being able to easily determine what the input plaintext was!

That being said, the course staff would like to reinforce a message commonly said by the security and privacy community: **"Never roll your own crypto"**. In other words, **do not** use this assignment in any future applications where you'd like to encrypt some confidential user information. Classical ciphers are known to be remarkably weak against the capabilites of modern computation and thus anything encrypted with them should not be considered secure.
___
## Code
Below is a description of the encryption schemes you're required to implement in this assignment:
___
### Caesar.java
The Caesar Cipher is likely the most commonly known encryption algorithm. It consists of assigning each input character a unique output character, and replacing all characters appropriately when encoding / decoding. This mapping is provided via a shifter string. Vertically aligning this shifter string with the alphabet and looking at the corresponding columns allows us to see the appropriate character mappings. Consider the following example:

![Diagram that outlines the process for input string "ABCDEFG" and shifter "GCBEAFD", these strings are stacked one on top of the other and the mapping can be seen from the top and bottom values in each column.](./CaesarCipher.png)

Given the shifter string above, the plaintext "FAD" would be encoded into "FGE" and decoding the ciphertext "CGE" gives the plaintext "BAD". Your solution must implement the following methods:

```java
public Caesar()
```
Constructs a new Caesar Cipher with an empty shifter

```java
public Caesar(String shifter)
```
Constructs a new Ceaser Cipher with the provided shifter. Should throw an `IllegalArgumentException` if the length of the shifter doesn't match number of characters handled by our Ciphers, any individual character falls outside our range of valid characters, or no encryption occurs with the given shifter.

```java
public setShifter(String shifter)
```
Updates the shifter for this Ceaser Cipher. Should throw an `IllegalArgumentException` if the length of the shifter doesn't match the number of valid characters handled by our Ciphers, any individual character falls outside our range of valid characters, or no encryption occurs with the given shifter.

Since we're allowing clients to set a shifter after construction, your `handleInput` method should throw an `IllegalStateException` if a shifter was never set (and thus no encryption/decryption can occur).
___
### CaesarShift.java

This encryption scheme is extremely similar to the one described above, except it involves shifting the entire alphabet forwards (or backwards) by the provided shift amount. Consider the following example:

![Diagram that outlines how to generate a shifter string with every character shifted forward 3 places, looping back to the start if needed](./CaesarShift.png)

Note that the shifter string consists of the alphabet with each character shifted forwards 3 places (looping back to the beginning if required). Below is the method signature for the classes' constructor:

```java
public CaesarShift(int shift)
```
An `IllegalArgumentException` should be thrown in the case that shift is a multiple of the number of valid encryptable characters (as no encryption would be occuring).

**HINT**: One way of viewing the modulo operator (%) is that it shrinks our integer numberline to one having a certain length (i.e. %10 only allows numbers +/- 0-9 to exist). This is useful in situations where we want to loop back to the beginning after passing a specific value and will likely be useful in your solution to the above.
___
### CaesarKey.java

Much like the CaesarShift, the CaesarKey scheme also builds off of the base Caesar Cipher. Namely, this one involves a key that is placed at the front of the shifter string, with the rest of the alphabet following normally (minus the characters included in the key). Consider the following example:

![Diagram that outlines created a shifter from a key. The key is first, and the rest of the alphabet (minus the keyed characters) follow in order](./CaesarKey.png)

Note that the shifter string starts with "BAG" (the key) and then is followed by the alphabet in its original order (minus the characters B, A, and G as they're already in the shifter). Below is the method signature for the classes' constructor:
```java
public CaesarKey(String key)
```
This constructor should throw an `IllegalArgumentException` in the case that the key is empty, it contains a character outside our range of valid characters, it contains any duplicate characters, or no encryption occurs with the given key.
___
### MultiCipher.java
The above ciphers are interesting, but on their own they're pretty solvable. A more complicated approach would be to chain these ciphers together to really confuse any possible adversaries! This is what you'll be implementing in this class: given a list of ciphers, use all of them them in order to encrypt / decrypt a message. Below is the appropriate method signature for the constructor:
```java
public MultiCipher(List<Cipher> ciphers)
```
An `IllegalArgumentException` should be thrown in the case that the list is empty (as no encryption would be occuring).
___
Now that you're done, create a MultiCipher consisting of the following: a CaesarShift(4), a CaesarKey("123"), a CaesarShift(12), and a CaesarKey("lemon"). Decrypt the following!

`Nelwnq! (el(vyly xylw(!xy (q  ywl}ul!)(Ne"|t(&e"(!u||($xq!(!xy (}u  qwu($q (ruvenu(tusetylwJ(E1`
___
## Creative Portion
For the creative portion of this assignment, you'll be implementing another cipher that interests you! Below is the recommended list:

1) Concealment
2) Vigenere
3) Transposition
4) CaesarRandom
5) Your choice!

___
### 1. Concealment

This scheme involves confusing any potential adversary with a jumble of random characters, and placing the important characters at specific locations within the encrypted message. For example, looking at the character following two filler characters in the below string reveals the message:

![A diagram showing a concealment cipher with a filler of 2 and a ciphertext of "k:S{jE?TCz=RSkEo\]T](./Concealment.png)

Your solution should contain the following constructor:
```java
public Concealment(int filler)
```
An `IllegalArgumentException` should be thrown in the case that filler is not positive.
___
### 2. Vigenere

The Vigenère cipher is a hybrid between the CaesarKey and CaesarShift. It is created with a key that is repeated such that its length matches that of the input text:

![A diagram showing how a key is repeated to match length input "HELLO", key "CSE" becomes "CSECS"](./Vigenere1.png)

This value at each position of this key then determines the CaesarShift to use for a specific character. If you imagine that the example above is only using uppercase alphabetic characters, that would mean index 0 would shift by 2 (c - a), index 1 would shift by 18 (s - a), index 2 would shift by 4 (e - a), and so on.

![A diagram showing how each of the shifts affect an character of the input string (H + 2 => J, E + 18 => W, L + 4 => P, L + 2 => N, O + 18 => G)](./Vigenere2.png)

This results in a ciphertext of "JWPNG". Another way to envision this is using a Vigenère square pictured below

![A diagram showing a vigenere square, which is a matrix of alphabet sequences, the first one starting "ABCD", the next starting "BCDE", the next "CDEF" and so on for every starting character](./VigenereGrid.png)

To encode, use the current key character to determine the row (total shift value) and the current input character at the top of the square to determine the column. To decode, again use the current key character as the row, but this time determine the column from the row itself. Following the column to the top shows the correct decoded character.

![A diagram showing how the rows of a Vigenere cipher are picked from the key character, and the column is picked from the input character. Finding where the row and column intercept gives the encrypted character](./VigenereGridFilled.png)

**Before continuing**, trace through the above example and make sure you understand how to encode and decode an input given a key. What changes when we are considering more than just alphabetic characters?

Below is the appropriate constructor signature for your solution:

```java
public Vigenere(String key)
```
An `IllegalArgumentException` should be thrown if the key is empty, or if it contains only `Cipher.MIN_CHAR` (as no encryption will occur).
___
### 3. Transposition

Unlike our previous ciphers, a transposition cipher involves shuffling the position of characters rather than substituting them with new ones. Most of these involve creating a grid with a certain width, filling it in with an input string, and then traversing the grid in a different way to get the encryption. For example:

![A diagram showing how to create an encryption grid from input "HELLO" and width 2. The resulting grid is [['H', 'E'], ['L', 'L'], ['O', '~']] and the output is "HLOEL~"](./Transposition.png)

Here, the grid was filled in by traversing rows, and the cipher was created by traversing columns. Decrypting would involve the opposite, filling in the grid by traversing columns, and creating the plaintext by traversing rows. Alternative traversals are possible, but we recommend this approach as it is the easiest to implement. You should also fill blank spots in the grid with the character given by `Cipher.MAX_VALUE` + 1 (~'s in the diagram above).

Below is the appropriate constructor signature for your solution:
```java
public Transposition(int width)
```
An `IllegalArgumentException` should be thrown if the width is <= 0 (not possible) or == 1 (as no encryption will occur).

Additionally, when decrypting, you should throw an `IllegalArgumentException` when the length of the input is not a multiple of width (meaning it is not possible to fully populate the grid with / it isn't a valid encryption).
___
### 4. CaesarRandom
Here, you'll implement another variation of a Caesar Cipher that uses a randomly shuffled shifter string. This initially sounds impossible as if we randomly create the shifter string, how do we possibly decrypt? The answer lies in being able to control a Random object in java via a seed value. Any two Random objects constructd with the same seed will produce random values in the same order as one another. Below is an example:
```java
int seed = 123;
Random rand = new Random(seed);
Random rand2 = new Random(seed);
System.out.println(rand.nextInt(10) == rand2.nextInt(10));
```
Thus, if you know the seed used to randomly create a shifter string, you can recreate it when decrypting. Your solution should store this seed somewhere in the encrypted message such that it is retreivable on decryption (i.e. front, end, etc.).

**HINT**: There exists a method that will shuffle the values of a list with a given random object called `Collections.shuffle(list, rand)`. You may use this if you'd like.

You should randomly generate a new seed every time you encrypt a message. The length of the seed will be determined by a number of digits provided by the constructor. It is your choice if you want to include leading 0's in the number of digits a number has. Alternatively stated, you get to pick whether given 3 digits if the smallest number will be 000 or 100.

Below is the appropriate constructor signature for your solution:

```java
public CaesarRandom(int digits)
```
An `IllegalArgumentException` should be thrown if digits isn't positive or if it is greater than the max number of digits for an integer, which is 9. (Note that the max integer value is 2,147,483,647 which is 10 digits, but larger 10-digit numbers can't be represented).
___
### 5. Your choice!

Here, you'll implement an encryption scheme that sounds most interesting to you! There are no constraints on this option, other than your encryption scheme must be 1:1 (every output sequence must have a *single* unique input sequence).
