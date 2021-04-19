package src.keys;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CertReader {

    public static X509Certificate get(String filename) throws Exception {
        InputStream inputStream = new FileInputStream(filename);
        return get(inputStream);
    }

    public static X509Certificate getFromString(String certString) throws Exception {
        byte[] certBytes = Base64.getDecoder().decode(certString);
        InputStream inputStream = new ByteArrayInputStream(certBytes);
        return get(inputStream);
    }

    public static X509Certificate get(InputStream inputStream) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(inputStream);
    }
}
