import java.io.*;
import java.net.*;
import java.util.Scanner;

public class HillCipherClient {
    private static final int MATRIX_SIZE = 3;

    // Generates the key matrix for the key string
    static void getKeyMatrix(String key, int keyMatrix[][]) {
        int k = 0;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                keyMatrix[i][j] = (key.charAt(k) - 'A') % 26;
                k++;
            }
        }
    }

    // Encrypts the message using the key matrix
    static void encrypt(int cipherMatrix[][], int keyMatrix[][], int messageVector[][]) {
        int x, i, j;
        for (i = 0; i < MATRIX_SIZE; i++) {
            for (j = 0; j < 1; j++) {
                cipherMatrix[i][j] = 0;
                for (x = 0; x < MATRIX_SIZE; x++) {
                    cipherMatrix[i][j] += keyMatrix[i][x] * messageVector[x][j];
                }
                cipherMatrix[i][j] = cipherMatrix[i][j] % 26;
            }
        }
    }

    // Prints matrix values
    static void printMatrix(String name, int matrix[][]) {
        System.out.println(name + ":");
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);

            // Create Scanner object for user input
            Scanner scanner = new Scanner(System.in);

            // Ask user for the message and key
            System.out.print("Enter the message (must be 3 letters): ");
            String message = scanner.nextLine().toUpperCase();
            if (message.length() != MATRIX_SIZE) {
                System.out.println("Message length must be " + MATRIX_SIZE + " characters.");
                return;
            }

            System.out.print("Enter the key (must be " + (MATRIX_SIZE * MATRIX_SIZE) + " letters): ");
            String key = scanner.nextLine().toUpperCase();
            if (key.length() != MATRIX_SIZE * MATRIX_SIZE) {
                System.out.println("Key length must be " + (MATRIX_SIZE * MATRIX_SIZE) + " characters.");
                return;
            }

            int[][] keyMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
            getKeyMatrix(key, keyMatrix);
            printMatrix("Key Matrix", keyMatrix);

            int[][] messageVector = new int[MATRIX_SIZE][1];
            for (int i = 0; i < MATRIX_SIZE; i++)
                messageVector[i][0] = (message.charAt(i) - 'A') % 26;

            // Print message vector
            System.out.println("Message Vector:");
            for (int i = 0; i < MATRIX_SIZE; i++)
                System.out.println(messageVector[i][0]);

            int[][] cipherMatrix = new int[MATRIX_SIZE][1];
            encrypt(cipherMatrix, keyMatrix, messageVector);

            // Print cipher matrix
            System.out.println("Cipher Matrix:");
            for (int i = 0; i < MATRIX_SIZE; i++)
                System.out.println(cipherMatrix[i][0]);

            // Convert cipher matrix to string
            StringBuilder cipherText = new StringBuilder();
            for (int i = 0; i < MATRIX_SIZE; i++)
                cipherText.append((char) (cipherMatrix[i][0] + 'A'));

            // Print encrypted message
            System.out.println("Encrypted Message: " + cipherText.toString());

            // Send encrypted message and key to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(cipherText.toString());
            out.println(key);

            socket.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

