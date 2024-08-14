import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class affineClient {
    private static final int ALPHABET_SIZE = 26;

    // Encrypt the plaintext using Affine cipher
    public static String encrypt(String plaintext, int a, int b) {
        StringBuilder encrypted = new StringBuilder();

        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int x = c - base;
                int y = (a * x + b) % ALPHABET_SIZE;
                encrypted.append((char) (y + base));
            } else {
                encrypted.append(c); // Non-alphabetic characters remain unchanged
            }
        }
        return encrypted.toString();
    }

    // Check if two numbers are coprime
    private static boolean areCoprime(int a, int b) {
        return gcd(a, b) == 1;
    }

    // Compute the greatest common divisor of two numbers
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Ask for plaintext and key values
            System.out.print("Enter the plaintext to encrypt: ");
            String plaintext = scanner.nextLine();

            System.out.print("Enter the key 'a' (an integer between 1 and 25): ");
            int a = scanner.nextInt();

            System.out.print("Enter the key 'b' (an integer between 0 and 25): ");
            int b = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            // Validate keys
            if (a < 1 || a >= ALPHABET_SIZE) {
                System.out.println("Invalid key 'a'. It must be between 1 and " + (ALPHABET_SIZE - 1));
                return;
            }

            if (b < 0 || b >= ALPHABET_SIZE) {
                System.out.println("Invalid key 'b'. It must be between 0 and " + (ALPHABET_SIZE - 1));
                return;
            }

            if (!areCoprime(a, ALPHABET_SIZE)) {
                System.out.println("Key 'a' and alphabet size must be coprime. 'a' is not valid.");
                return;
            }

            // Encrypt the plaintext
            String encryptedMessage = encrypt(plaintext, a, b);

            // Print the encrypted message
            System.out.println("Encrypted message: " + encryptedMessage);

            // Send the encrypted message and keys to the server
            output.println(encryptedMessage);
            output.println(a);
            output.println(b);

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}

