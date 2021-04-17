package src.nonce;

import java.security.SecureRandom;

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
}
