package src.server;

import java.security.PublicKey;

public abstract class ServerCert {

    public abstract void checkValidity();

    public abstract void verify(PublicKey key);
}
