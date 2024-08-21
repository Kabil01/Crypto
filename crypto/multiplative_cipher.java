import java.util.*;

public class multiplative_cipher {
    public static void main(String[] args) {
        String string = "charan";

        Scanner scanner = new Scanner(System.in);
        System.out.println("enter the string:");
        String input = scanner.nextLine();
        System.out.println("enter the key value");
        int key = scanner.nextInt();
        String encrypt = encryption(input, key);
        System.out.println("encripted value is" + encrypt);

        String decrypt = decryption(encrypt, key);
        System.out.println("decripted value is" + decrypt);

    }

    public static String encryption(String input, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ascii = (input.charAt(i));
            if (Character.isUpperCase(ascii)) {
                char x = (char) (((ascii - 'A') * key % 26 + 26) % 26 + 'A');
                encrypted.append(x);
            } else {
                char x = (char) (((ascii - 'a') * key % 26 + 26) % 26 + 'a');
                encrypted.append(x);
            }
        }
        return encrypted.toString();
    }

    public static String decryption(String encrypted, int key) {
        StringBuilder decrypted = new StringBuilder();
        int inversekey = findinversekey(key);
        if (inversekey == -1) {
            return "cannot be decrypted";
        }
        for (int i = 0; i < encrypted.length(); i++) {
            char ascii = encrypted.charAt(i);
            if (Character.isUpperCase(ascii)) {
                char x = (char) (((ascii - 'A') * inversekey % 26 + 26) % 26 + 'A');
                decrypted.append(x);
            } else {
                char x = (char) (((ascii - 'a') * inversekey % 26 + 26) % 26 + 'a');
                decrypted.append(x);
            }

        }
        return decrypted.toString();
    }

    public static int findinversekey(int key) {
        for (int i = 1; i < 26; i++) {
            if ((key * i) % 26 == 1) {
                return i;
            }
        }
        return -1;
    }
}