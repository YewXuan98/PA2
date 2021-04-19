package src.server;

import src.constant.Packet;
import src.nonce.NonceGen;
import src.rsa.cipher.ByteGen;
import src.rsa.cipher.CipherGen;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

import static src.server.Server_CP1.*;

public class Server_CP2 {

    private static final byte[] NONCE = NonceGen.generateNonce();

    public static void main(String[] args) throws Exception {
        verifyClient();
        int packetType = fromClient.readInt();
        int packetCount = 0;

        SecretKey AESKey = new SecretKeySpec(NONCE, 0, 12, "AES");
        switch (packetType) {
            case Packet.C2: {
                String symKey = fromClient.readUTF();

                // TODO: Decrypt and decode the AES Key
                byte[] decodedKey = CipherGen.decryptCipher(serverPrivateKey,
                        Base64.getDecoder().decode(symKey), 1);
                System.out.println("Received symmetric key");
                AESKey = new SecretKeySpec(decodedKey, 12, decodedKey.length, "AES");
                symKey = ByteGen.getBase64Format(AESKey.getEncoded());

                System.out.println("symKey is: " + symKey + "\n");
                break;
            }
            case Packet.C3: {

                int numBytes = fromClient.readInt();
                int numBytesEncrypted = fromClient.readInt();
                byte[] block = new byte[numBytesEncrypted];
                fromClient.readFully(block, 0, numBytes);
                packetCount++;

                byte[] blockDecrypted = CipherGen.decryptCipher(AESKey, block,1);

                if (numBytes > 0) {
                    bufferedFileOutputStream.write(blockDecrypted, 0, numBytes);
                }
                if (numBytes < 117) {
                    System.out.println("Closing connection...");
                    System.out.println("Total packets received: " + packetCount);

                    if (bufferedFileOutputStream != null) bufferedFileOutputStream.close();
                    if (bufferedFileOutputStream != null) fileOutputStream.close();
                    fromClient.close();
                    toClient.close();
                    connectionSocket.close();
                }
            }
            case Packet.END: {
                System.out.println("Closing connection...");
                fromClient.close();
                toClient.close();
                connectionSocket.close();
            }
        }
    }
}
