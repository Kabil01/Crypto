import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CaesarServer {
    // Decrypt the ciphertext using Caesar cipher
    public static String decrypt(String ciphertext, int shift) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            char base = Character.isUpperCase(c) ? 'A' : 'a';
            decrypted.append((char) ((c - base - shift + 26) % 26 + base));
        }
        return decrypted.toString();
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
                    // Read the shift value from the client
                    int shift = Integer.parseInt(input.readLine());
		     System.out.println("Encrypted message received from Client: " + encryptedMessage);
                    // Decrypt the message
                    String decryptedMessage = decrypt(encryptedMessage, shift);

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

