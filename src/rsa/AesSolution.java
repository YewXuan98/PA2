package src.rsa;

import src.digest.DigitalSignatureSolution;

import javax.crypto.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class AesSolution {

    public static final String
            ENCRYPTION = "AES",
            ECB = "ECB",
            BLOCK_SIZE = "PKCS5Padding";

    private static final AesSolution aesSoln = new AesSolution();

    private static void desEncrypt(String file, StringBuilder sb) {
        SecretKey desKey = aesSoln.generateSecretKey();

        Cipher desCipher = aesSoln.initCipher(Cipher.ENCRYPT_MODE, desKey);

        byte[] encryptedBytesArray = aesSoln.generateEncryptedByte(desCipher, sb.toString());
        if (encryptedBytesArray != null) {
            //TODO: print the length of output encrypted byte[], compare the length of file shorttext.txt and longtext.txt
            System.out.printf("Length of %s is %d\n", file, encryptedBytesArray.length);

            //TODO: Question 3
            System.out.println(new String(encryptedBytesArray));

            //TODO: print the encrypted message (in base64format String format)
            System.out.println("\nEncrypted message content:");
            System.out.println(aesSoln.getBase64Format(encryptedBytesArray));

            //TODO: create cipher object, initialize the ciphers with the given key, choose decryption mode as DES
            Cipher desCipher2 = aesSoln.initCipher(Cipher.DECRYPT_MODE, desKey);

            //TODO: do decryption, by calling method Cipher.doFinal().
            byte[] decryptedBytesArray = aesSoln.generateByte(desCipher2, encryptedBytesArray);
            if (decryptedBytesArray != null) {
                //TODO: do format conversion. Convert the decrypted byte[] to String, using "String a = new String(byte_array);"
                String decryptedString = new String(decryptedBytesArray);

                //TODO: print the decrypted String text and compare it with original text
                System.out.println(decryptedString);
                System.out.println();
            }
        }
    }

    //TODO: do format conversion. Turn the encrypted byte[] format into base64format String using Base64
    public static String getBase64Format(byte[] byteArray) {
        //TODO: do format conversion. Turn the encrypted byte[] format into base64format String using Base64
        return Base64.getEncoder().encodeToString(byteArray);
    }

    //TODO: generate secret key using DES algorithm
    public static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO: create cipher object, initialize the ciphers with the given key, choose encryption mode as DES
    public static Cipher initCipher(int mode, SecretKey key) {
        return DigitalSignatureSolution.getCipher(ECB, mode, key, ENCRYPTION, BLOCK_SIZE);
    }

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


}