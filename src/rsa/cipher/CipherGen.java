package src.rsa.cipher;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Generates cipher
 */

public class CipherGen {

    public static final String
            ECB = "ECB",
            RSA = "RSA",
            BLOCK_SIZE = "PKCS1Padding";

    public static Cipher initCipher(int mode, Key key) {
        return getCipher(mode, key);
    }

    public static Cipher getCipher(int mode, Key key) {
        try {
            Cipher rsaCipher = Cipher.getInstance(String.format("%s/%s/%s", RSA, ECB, BLOCK_SIZE));
            rsaCipher.init(mode, key);
            return rsaCipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptCipher(Key key, byte[] encryptedBytesArray) {

        //TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key.
        Cipher rsaCipher = CipherGen.initCipher(Cipher.DECRYPT_MODE, key);

        //TODO: decrypt message
        byte[] decryptedBytesArray = ByteGen.generateByte(rsaCipher, encryptedBytesArray);

        //TODO: print the decrypted message (in base64format String using Base64), compare with origin digest
        return decryptedBytesArray;
    }

    public static byte[] encryptCipher(Key key, byte[] message) {
        //TODO: Set the mode
        Cipher rsaCipher = CipherGen.initCipher(Cipher.ENCRYPT_MODE, key);

        //TODO: encrypt the message(byte array type)
        return ByteGen.generateByte(rsaCipher, message);
    }

}
