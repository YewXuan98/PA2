package src.digest;

import src.rsa.AesSolution;
import src.rsa.cipher.ByteGen;
import src.rsa.cipher.CipherGen;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;


public class DigitalSignatureSolution {

    public static final String
            ENCRYPTION = "RSA",
            BLOCK_SIZE = "PKCS1Padding";

    private static Key publicKey, privateKey;

    public static void main(String[] args) {
    }

    private static void rsaEncrpt(String file, StringBuilder sb) {
        AesSolution desSoln = new AesSolution();

        DigitalSignatureSolution digSigSoln = new DigitalSignatureSolution();
        digSigSoln.generateRsaKeyPair();

        //TODO: print the length of output digest byte[], compare the length of file shorttext.txt and longtext.txt
        byte[] digest = digSigSoln.computeDigest(sb.toString());
        if (digest != null) {
            System.out.printf("\nLength of %s is %d\n", file, digest.length);

            //TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as encrypt mode, use PRIVATE key.
            byte[] encryptedBytesArray = CipherGen.encryptCipher(privateKey, digest, 0);

            //TODO: encrypt digest message
//            byte[] encryptedBytesArray = ByteGen.generateByte(rsaCipher, digest);

            //TODO: print the encrypted message (in base64format String using Base64)
            System.out.println("\nEncrypted message content:");
            System.out.println(ByteGen.getBase64Format(encryptedBytesArray));

            //TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key.
            byte[] decryptedBytesArray = CipherGen.decryptCipher(publicKey, encryptedBytesArray, 0);

            //TODO: decrypt message
//            byte[] decryptedBytesArray = ByteGen.generateByte(rsaCipher, encryptedBytesArray);

            //TODO: print the decrypted message (in base64format String using Base64), compare with origin digest
            String decryptedMessage = ByteGen.getBase64Format(decryptedBytesArray);
            if (decryptedMessage.equals(ByteGen.getBase64Format(digest))) {
                System.out.printf("\nThe decrypted message length is unchanged\n%s", decryptedMessage);
            }
        }
    }

    //TODO: generate a RSA keypair, initialize as 1024 bits, get public key and private key from this keypair.
    private void generateRsaKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ENCRYPTION);
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //TODO: Calculate message digest, using MD5 hash function
    private byte[] computeDigest(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cipher initCipher(String type, int mode, Key key) {
        return getCipher(type, mode, key, ENCRYPTION, BLOCK_SIZE);
    }

    public static Cipher getCipher(String type, int mode, Key key, String encryption, String blockSize) {
        try {
            Cipher rsaCipher = Cipher.getInstance(String.format("%s/%s/%s", encryption, type, blockSize));
            rsaCipher.init(mode, key);
            return rsaCipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}