import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class affineServer {
    // Decrypt the ciphertext using Affine cipher
    public static String decrypt(String ciphertext, int a, int b) {
        StringBuilder decrypted = new StringBuilder();
        int m = 26; // Alphabet size

        // Compute modular inverse of 'a'
        int a_inv = modInverse(a, m);

        if (a_inv == -1) {
            throw new IllegalArgumentException("No modular inverse for the given 'a'");
        }

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int y = c - base;
                int x = (a_inv * (y - b + m)) % m;
                decrypted.append((char) (x + base));
            } else {
                decrypted.append(c); // Non-alphabetic characters remain unchanged
            }
        }
        return decrypted.toString();
    }

    // Calculate modular inverse of 'a' under modulo 'm'
    public static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // Modular inverse does not exist
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                    System.out.println("Client connected");

                    // Read the encrypted message from the client
                    String encryptedMessage = input.readLine();
                    // Read the key values from the client
                    int a = Integer.parseInt(input.readLine());
                    int b = Integer.parseInt(input.readLine());
		     System.out.println("Encrypted message from Client is: " + encryptedMessage);
                    // Decrypt the message
                    String decryptedMessage = decrypt(encryptedMessage, a, b);

                    // Print the decrypted message
                    System.out.println("Decrypted message: " + decryptedMessage);
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}

