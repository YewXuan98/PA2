package src.rsa.cipher;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.util.Base64;

/**
 * GeneratesBytes as output
 */

public class ByteGen {

    public static byte[] generateEncryptedByte(Cipher c, String s) {
        return generateByte(c, s.getBytes());
    }

    public static byte[] generateByte(Cipher c, byte[] b) {
        if (c != null && b != null) {
            //TODO: do encryption, by calling method Cipher.doFinal().
            try {
                return c.doFinal(b);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //TODO: do format conversion. Turn the encrypted byte[] format into base64format String using Base64
    public static String getBase64Format(byte[] byteArray) {
        //TODO: do format conversion. Turn the encrypted byte[] format into base64format String using Base64
        return Base64.getEncoder().encodeToString(byteArray);
    }
}
