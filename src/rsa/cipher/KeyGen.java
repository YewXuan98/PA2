package src.rsa.cipher;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Generate pair of keys
 */

public class KeyGen {

    private static final String ENCRYPTION = "RSA";

    //TODO: generate a RSA keypair, initialize as 1024 bits, get public key and private key from this keypair.
    public Key[] generateRsaKeyPair() {
        Key[] key = new Key[2];
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ENCRYPTION);
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();
            key[0] = keyPair.getPublic();
            key[1] = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key;
    }
}
