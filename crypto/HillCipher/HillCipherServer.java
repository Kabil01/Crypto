import java.io.*;
import java.net.*;

public class HillCipherServer {
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

    // Decrypts the cipher matrix using the inverse key matrix
    static void decrypt(int plainMatrix[][], float inverseKeyMatrix[][], int cipherMatrix[][]) {
        int x, i, j;
        for (i = 0; i < MATRIX_SIZE; i++) {
            for (j = 0; j < 1; j++) {
                plainMatrix[i][j] = 0;
                for (x = 0; x < MATRIX_SIZE; x++) {
                    plainMatrix[i][j] += inverseKeyMatrix[i][x] * cipherMatrix[x][j];
                }
                plainMatrix[i][j] = (plainMatrix[i][j] % 26 + 26) % 26; // Handle negative values
            }
        }
    }

    // Finds the matrix determinant
    static int determinant(int matrix[][], int n) {
        int result = 0;
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        int[][] temp = new int[n][n];
        int sign = 1;
        for (int f = 0; f < n; f++) {
            getCofactor(matrix, temp, 0, f, n);
            result += sign * matrix[0][f] * determinant(temp, n - 1);
            sign = -sign;
        }
        return result;
    }

    // Gets the cofactor of matrix
    static void getCofactor(int matrix[][], int temp[][], int p, int q, int n) {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = matrix[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    // Finds the adjugate of matrix
    static void adjugate(int matrix[][], int adj[][]) {
        int sign = 1;
        int[][] temp = new int[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                getCofactor(matrix, temp, i, j, MATRIX_SIZE);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = sign * determinant(temp, MATRIX_SIZE - 1);
            }
        }
    }

    // Finds the inverse of matrix
    static boolean inverse(int matrix[][], float inverse[][]) {
        int det = determinant(matrix, MATRIX_SIZE);
        if (det == 0) {
            System.out.println("Singular matrix, can't find its inverse");
            return false;
        }
        int adj[][] = new int[MATRIX_SIZE][MATRIX_SIZE];
        adjugate(matrix, adj);
        int invDet = modInverse(det, 26);
        System.out.println("Adjugate Matrix:");
        printMatrix("Adjugate Matrix", adj);
        System.out.println("Determinant: " + det);
        System.out.println("Modular Inverse of Determinant: " + invDet);

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                inverse[i][j] = adj[i][j] * invDet;
                inverse[i][j] = ((int)inverse[i][j] % 26 + 26) % 26;
            }
        }
        System.out.println("Inverse Key Matrix:");
        printMatrix("Inverse Key Matrix", inverse);
        return true;
    }

    // Finds modular inverse
    static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;
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

    // Prints matrix values for float matrix
    static void printMatrix(String name, float matrix[][]) {
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
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is running...");

            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String cipherText = in.readLine();
            String key = in.readLine();

            System.out.println("Received Cipher Text: " + cipherText);
            System.out.println("Received Key: " + key);

            int[][] keyMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
            getKeyMatrix(key, keyMatrix);
            System.out.println("Key Matrix:");
            printMatrix("Key Matrix", keyMatrix);

            int[][] cipherMatrix = new int[MATRIX_SIZE][1];
            for (int i = 0; i < MATRIX_SIZE; i++) {
                cipherMatrix[i][0] = cipherText.charAt(i) - 'A'; // Convert char to int
            }
            System.out.println("Cipher Matrix:");
            for (int i = 0; i < MATRIX_SIZE; i++) {
                System.out.println(cipherMatrix[i][0]);
            }

            float[][] inverseKeyMatrix = new float[MATRIX_SIZE][MATRIX_SIZE];
            if (inverse(keyMatrix, inverseKeyMatrix)) {
                int[][] decryptedMatrix = new int[MATRIX_SIZE][1];
                decrypt(decryptedMatrix, inverseKeyMatrix, cipherMatrix);

                StringBuilder decryptedText = new StringBuilder();
                for (int i = 0; i < MATRIX_SIZE; i++)
                    decryptedText.append((char) (decryptedMatrix[i][0] + 'A'));

                System.out.println("Decrypted message: " + decryptedText.toString());
            } else {
                System.out.println("Failed to compute the inverse matrix. Decryption failed.");
            }

            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

