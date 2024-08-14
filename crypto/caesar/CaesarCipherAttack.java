import java.util.Scanner;
public class CaesarCipherAttack {

    // Method to decrypt a message with a given shift
    static String decrypt(String cipherText, int shift) {
        StringBuilder decryptedText = new StringBuilder();
        shift = shift % 26; // Handle shifts greater than 26
        for (char i : cipherText.toCharArray()) {
            if (Character.isLetter(i)) {
                char base = Character.isUpperCase(i) ? 'A' : 'a';
                decryptedText.append((char) ((i - base - shift + 26) % 26 + base));
            } else {
                decryptedText.append(i);
            }
        }
        return decryptedText.toString();
    }

    // Method to try all possible shifts
    static void bruteForceAttack(String cipherText) {
        for (int shift = 1; shift < 26; shift++) {
            String decryptedText = decrypt(cipherText, shift);
            System.out.println("Shift " + shift + ": " + decryptedText);
        }
    }

    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Enter the Cipher Text:");
    	String cipherText = scanner.nextLine();	
        System.out.println("Trying to decrypt with Caesar Cipher Brute Force:");
        bruteForceAttack(cipherText);
    }
}

