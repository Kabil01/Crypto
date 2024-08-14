class HillCipherExample {
    private static final int MATRIX_SIZE = 3;

    // Generates the key matrix for the key string
    static void getKeyMatrix(String key, int keyMatrix[][]) {
        int k = 0;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                keyMatrix[i][j] = (key.charAt(k)) % 65;
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
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                inverse[i][j] = adj[i][j] * invDet;
                inverse[i][j] = ((int)inverse[i][j] % 26 + 26) % 26;
            }
        }
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

    // Implements Hill Cipher
    static void HillCipher(String message, String key) {
        int[][] keyMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
        getKeyMatrix(key, keyMatrix);

        int[][] messageVector = new int[MATRIX_SIZE][1];
        for (int i = 0; i < MATRIX_SIZE; i++)
            messageVector[i][0] = (message.charAt(i)) % 65;

        int[][] cipherMatrix = new int[MATRIX_SIZE][1];
        encrypt(cipherMatrix, keyMatrix, messageVector);

        String CipherText = "";
        for (int i = 0; i < MATRIX_SIZE; i++)
            CipherText += (char) (cipherMatrix[i][0] + 65);

        System.out.println("Ciphertext: " + CipherText);

        // Decrypt the message
        float[][] inverseKeyMatrix = new float[MATRIX_SIZE][MATRIX_SIZE];
        if (inverse(keyMatrix, inverseKeyMatrix)) {
            int[][] decryptedMatrix = new int[MATRIX_SIZE][1];
            decrypt(decryptedMatrix, inverseKeyMatrix, cipherMatrix);

            String decryptedText = "";
            for (int i = 0; i < MATRIX_SIZE; i++)
                decryptedText += (char) (decryptedMatrix[i][0] + 65);

            System.out.println("Decrypted message: " + decryptedText);
        }
    }

    public static void main(String[] args) {
        String message = "ACT";
        String key = "GYBNQKURP";

        HillCipher(message, key);
    }
}

