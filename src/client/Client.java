package src.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Client {

    public static PublicKey getPublicKey() {
        InputStream fis = null;
        CertificateFactory cf = null;
        try {
            fis = new FileInputStream("CA.crt");
            cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(fis).getPublicKey();
        } catch (FileNotFoundException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public PublicKey getPublicKey(X509Certificate caCert){
//        return caCert.getPublicKey();
//    }
}
