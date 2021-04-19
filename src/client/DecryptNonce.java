package src.client;

import src.io.IO;
import src.keys.ca.PrivateKeyReader;
import src.keys.ca.PublicKeyReader;
import src.rsa.cipher.CipherGen;

import java.security.PrivateKey;
import java.security.PublicKey;

public class DecryptNonce {

    public static void main(String[] args) throws Exception {
        PublicKey caKey = PublicKeyReader.get("public_key.der");; //public key from CA
//        PublicKey clientPublicKey = Client.getPublicKey();
        System.out.println(caKey);
//        System.out.println(clientPublicKey);
//        System.out.println(caKey.equals(clientPublicKey));

        PrivateKey key = PrivateKeyReader.get("private_key.der");
        // TODO: encrypt with server's private key (private_key.der)
        byte[] b = CipherGen.encryptCipher(key, IO.fileReader("src/textfile/nonce.txt").toString().getBytes());
        IO.fileWriter("src/textfile/nonce_r.txt", String.valueOf(b).trim());


        String s = IO.fileReader("src/textfile/nonce.txt").toString();
        System.out.println(s);

        byte[] str = new byte[117];
        str = IO.fileReader("src/textfile/nonce_r.txt").toString().getBytes();
//        System.out.println(str);
//        Cipher cipher = CipherGen.initCipher(Cipher.DECRYPT_MODE, caKey);
        str = CipherGen.decryptCipher(caKey, str);

        System.out.println(str);

        System.out.println(s.equals(str.toString()));
    }


}
