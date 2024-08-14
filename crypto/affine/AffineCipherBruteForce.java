import java.util.Scanner;

public class AffineCipherBruteForce {

    private static final int ALPHABET_SIZE = 26;

    // Decrypts a message with given keys a and b
    static String decrypt(String cipherText, int a, int b) {
        StringBuilder decryptedText = new StringBuilder();
        int aInverse = modInverse(a, ALPHABET_SIZE);
        if (aInverse == -1) {
            return "Invalid 'a' value; no modular inverse exists.";
        }

        for (char i : cipherText.toCharArray()) {
            if (Character.isLetter(i)) {
                char base = Character.isUpperCase(i) ? 'A' : 'a';
                int x = (i - base - b + ALPHABET_SIZE) % ALPHABET_SIZE;
                decryptedText.append((char) ((x * aInverse % ALPHABET_SIZE) + base));
            } else {
                decryptedText.append(i);
            }
        }
        return decryptedText.toString();
    }

    // Finds modular inverse of a under modulo m
    static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // No modular inverse exists
    }

    // Performs brute force attack by trying all possible values of a and b
    static void bruteForceAttack(String cipherText) {
        for (int a = 1; a < ALPHABET_SIZE; a++) {
            if (gcd(a, ALPHABET_SIZE) == 1) { // a must be coprime with ALPHABET_SIZE
                for (int b = 0; b < ALPHABET_SIZE; b++) {
                    String decryptedText = decrypt(cipherText, a, b);
                    System.out.println("a = " + a + ", b = " + b + ": " + decryptedText);
                }
            }
        }
    }

    // Computes the greatest common divisor of a and b
    static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get ciphertext from user
        System.out.print("Enter the ciphertext to brute force: ");
        String cipherText = scanner.nextLine();

        System.out.println("Trying to decrypt with Affine Cipher Brute Force:");
        bruteForceAttack(cipherText);

        scanner.close();
    }
}

