import java.util.Scanner;

class vigenerecipher {

    // Generate the key by extending it to match the length of the plaintext without spaces
    static String generateKey(String str, String key) {
        StringBuilder extendedKey = new StringBuilder(key);
        int strLength = str.replace(" ", "").length(); // Count only characters excluding spaces

        while (extendedKey.length() < strLength) {
            extendedKey.append(key);
        }

        return extendedKey.substring(0, strLength);
    }

    // Encrypt the plaintext, skipping spaces
    static String encrypt(String str, String key) {
        StringBuilder ciphertext = new StringBuilder();
        int keyIndex = 0; // To track the position in the key

        for (char ch : str.toCharArray()) {
            if (Character.isLetter(ch)) { // Encrypt only letters
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char keyBase = Character.isUpperCase(key.charAt(keyIndex)) ? 'A' : 'a';

                int shift = (ch - base + (key.charAt(keyIndex) - keyBase)) % 26;
                if (shift < 0) shift += 26;

                ciphertext.append((char) (base + shift));
                keyIndex = (keyIndex + 1) % key.length(); // Move to the next key character
            } else {
                ciphertext.append(ch); // Append spaces and other characters unchanged
            }
        }
        return ciphertext.toString();
    }

    // Decrypt the ciphertext, skipping spaces
    static String decrypt(String cipherText, String key) {
        StringBuilder origText = new StringBuilder();
        int keyIndex = 0; // To track the position in the key

        for (char ch : cipherText.toCharArray()) {
            if (Character.isLetter(ch)) { // Decrypt only letters
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char keyBase = Character.isUpperCase(key.charAt(keyIndex)) ? 'A' : 'a';

                int shift = (ch - base - (key.charAt(keyIndex) - keyBase) + 26) % 26;

                origText.append((char) (base + shift));
                keyIndex = (keyIndex + 1) % key.length(); // Move to the next key character
            } else {
                origText.append(ch); // Append spaces and other characters unchanged
            }
        }
        return origText.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter plaintext: ");
        String plaintext = scanner.nextLine();

        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();

        // Generate key based on the plaintext (excluding spaces)
        String key = generateKey(plaintext, keyword);
        // Encrypt and decrypt the text
        String cipherText = encrypt(plaintext, key);

        System.out.println("Ciphertext: " + cipherText);

        String originalText = decrypt(cipherText, key);
        System.out.println("Decrypted Text: " + originalText);

        scanner.close();
    }
}
