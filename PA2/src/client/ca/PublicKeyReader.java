package src.client.ca;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;

public class PublicKeyReader {

    private static final String PUBLIC = "./public_key.der";

    public static PublicKey get() {
        try{
            byte[] keyBytes = Files.readAllBytes(Paths.get(PUBLIC));

            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch(Exception e){
            e.printStackTrace();
    }
        return  null;
}
}
