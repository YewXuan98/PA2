package src.nonce;

import src.io.IO;
import src.rsa.cipher.ByteGen;

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * Nonce:
 * To generate a OTP for 3 way secure handshake
 * Server will generate NONCE (DONE)
 */

public class NonceGen {

    public static byte[] generateNonce() {
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    public static void generateClientNonce() {
        byte[] clientBytenonce;
        clientBytenonce = NonceGen.generateNonce();
        String clientStringnonce = ByteGen.getBase64Format(clientBytenonce);
        String path = "src/textfile/nonce.txt";
        IO.fileWriter(path, clientStringnonce);
    }

}
