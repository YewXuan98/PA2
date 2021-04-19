package src.rsa.cipher;

import javax.crypto.BadPaddingException;
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
            BLOCK_SIZE = "PKCS1Padding",
            AES = "AES",
            BLOCK_SIZE_AES = "PKCS5Padding";

    public static Cipher initCipher(int mode, Key key, int type) {
        return getCipher(mode, key, type);
    }

    public static Cipher getCipher(int mode, Key key, int type) {
        try {
            if (type == 0) {
                Cipher rsaCipher = Cipher.getInstance(String.format("%s/%s/%s", RSA, ECB, BLOCK_SIZE));
                rsaCipher.init(mode, key);
                return rsaCipher;
            } else if (type == 1){
                Cipher aesCipher = Cipher.getInstance(String.format("%s/%s/%s", AES, ECB, BLOCK_SIZE_AES));
                aesCipher.init(mode, key);
                return aesCipher;
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptCipher(Key key, byte[] encryptedBytesArray, int type) {

        //TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key.
        Cipher rsaCipher = CipherGen.initCipher(Cipher.DECRYPT_MODE, key, type);

        //TODO: decrypt message
        byte[] decryptedBytesArray = ByteGen.generateByte(rsaCipher, encryptedBytesArray);

        //TODO: print the decrypted message (in base64format String using Base64), compare with origin digest
        return decryptedBytesArray;
    }

    public static byte[] encryptCipher(Key key, byte[] message, int type) {
        //TODO: Set the mode
        Cipher rsaCipher = CipherGen.initCipher(Cipher.ENCRYPT_MODE, key, type);

        //TODO: encrypt the message(byte array type)
        return ByteGen.generateByte(rsaCipher, message);
    }

}
