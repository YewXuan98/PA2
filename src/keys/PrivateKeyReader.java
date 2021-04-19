package src.keys;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKeyReader {

    public static PrivateKey get(String fileName) throws Exception{
        byte[]keyBytes=Files.readAllBytes(Paths.get(fileName));

        PKCS8EncodedKeySpec spec=
        new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf=KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);


    }




}
