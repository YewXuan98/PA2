package src.rsa.encrypt;

import src.rsa.cipher.KeyGen;

import javax.crypto.*;
import java.security.Key;


public class RsaGen {

    private static Key publicKey, privateKey;

    public static void main(String[] args) {
        KeyGen key = new KeyGen();

        publicKey = key.generateRsaKeyPair()[0];
        privateKey = key.generateRsaKeyPair()[1];


    }

    public byte[] generateByte(Cipher c, byte[] b) {
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

}
