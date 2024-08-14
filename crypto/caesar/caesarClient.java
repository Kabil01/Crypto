import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class CaesarClient {
    // Encrypt the plaintext using Caesar cipher
    public static String encrypt(String plaintext, int shift) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            char base = Character.isUpperCase(c) ? 'A' : 'a';
            encrypted.append((char) ((c - base + shift) % 26 + base));
        }
        return encrypted.toString();
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Ask for plaintext and shift value
            System.out.print("Enter the plaintext to encrypt: ");
            String plaintext = scanner.nextLine();

            System.out.print("Enter the shift value (an integer): ");
            int shift = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            // Encrypt the plaintext
            String encryptedMessage = encrypt(plaintext, shift);

            // Print the encrypted message
            System.out.println("Encrypted message sent to SERVER: " + encryptedMessage);

            // Send the encrypted message and shift value to the server
            output.println(encryptedMessage);
            output.println(shift);

            // Optionally, wait for a confirmation from the server
            // (This is just for demonstration and is not strictly required)
            // String confirmation = input.readLine();
            // System.out.println(confirmation);
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}

