package src.client;

import src.constant.Packet;
import src.keys.CertReader;
import src.nonce.NonceGen;
import src.rsa.cipher.CipherGen;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class Client_CP1 {

    public static final String NONCE = new String(NonceGen.generateNonce());
    public static final int port = 4321;
    public static final String serverAddress = "localhost";

    public static DataOutputStream toServer;
    public static DataInputStream fromServer;

    public static Socket clientSocket;
    public static X509Certificate serverCert;

    public static PublicKey serverPublicKey, caPubKey;
    public static String encryptedMessage;

    public static PublicKey getCaPublicKey() throws Exception {
        return CertReader.get("ca_pub.crt")
                .getPublicKey();
    }

    public static void verify() throws Exception {
        System.out.println("Establishing connection to server...");

        //TODO: get public key CA cert from server
        caPubKey = getCaPublicKey();

        System.out.println("CA Public Key: " + caPubKey + "\n");

        // Connect to server and get the input and output streams
        clientSocket = new Socket(serverAddress, port);
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        fromServer = new DataInputStream(clientSocket.getInputStream());

        System.out.println("Sending file...");

        //TODO: Do Authentication
        //sendMessage(NONCE, Packet.AUTH); //prove identity
        toServer.writeInt(Packet.AUTH);
        toServer.writeUTF(NONCE);
        encryptedMessage = fromServer.readUTF();

        toServer.writeInt(Packet.C1);

        // TODO: Receive cert from server
        System.out.println("Incoming server certificate");
        String serverCertString = fromServer.readUTF();
        serverCert = CertReader.getFromString(serverCertString);

        serverPublicKey = serverCert.getPublicKey();
        System.out.println("Server Public Key: " + serverPublicKey + "\n");

        //TODO: Verify server's certificate
        try {
            serverCert.checkValidity();
            serverCert.verify(caPubKey);
        } catch (Exception e) {
            e.printStackTrace();
            toServer.write(Packet.ERROR); //invalid cert, close connection
            System.out.println("Invalid cert...");
            System.out.println("Closing connection...");
            clientSocket.close();
        }

        //TODO: Verify server owns the private key by decrypting encrypted message w server's public key
        String decryptedMessage = new String(CipherGen.decryptCipher(serverPublicKey,
                Base64.getDecoder().decode(encryptedMessage), 0));

        if (decryptedMessage.compareTo(NONCE) != 0) {
            toServer.write(Packet.ERROR);

            System.out.println("Incorrect nonce...");
            System.out.println("Closing connection...");
            clientSocket.close();
            return;
        }
        System.out.println("Server's cert has been verified\n");
    }


    public static void main(String[] args) throws Exception {
        verify();

        int numBytes;

        FileInputStream fileInputStream;
        BufferedInputStream bufferedFileInputStream;

        long timeStarted = System.nanoTime();

        try {

            //TODO: Send files
            for (int i = 0; i < args.length; i++) {
                String fileName = args[i];

                //TODO: begin handshake for file upload
                System.out.println("Sending " + fileName + "...");
                sendMessage(fileName.getBytes(), Packet.C3);

                //TODO: Open the file
                fileInputStream = new FileInputStream(args[i]);
                bufferedFileInputStream = new BufferedInputStream(fileInputStream);

                byte[] fromFileBuffer = new byte[117];

                int packetCount = 200;
                //TODO: send 3 packets

                //TODO: numBytest = number of bytes before encryption, to be written
                // Send the file
                for (boolean fileEnded = false; !fileEnded;) {
                    // send 3 packets
                    // numBytes = number of bytes before encryption, to be written
                    // numBytesEncrypted = number of bytes after encryption, to be read from the buffer

                    numBytes = bufferedFileInputStream.read(fromFileBuffer);
                    fileEnded = numBytes < 117;

                    toServer.writeInt(Packet.C3); // 1 => file chunk
                    toServer.writeInt(numBytes);
                    // System.out.println(numBytes);

                    // System.out.println("original bytes: " + fromFileBuffer);
                    // System.out.println("before encryption length: " + fromFileBuffer.length);

                    // encrypt the data
                    byte[] fromFileBufferEncrypted = CipherGen
                            .encryptCipher(serverPublicKey, fromFileBuffer, 0);

                    int numBytesEncryted = fromFileBufferEncrypted.length;
                    toServer.writeInt(numBytesEncryted);
                    // System.out.println(numBytesEncryted);

                    // send the data
                    toServer.write(fromFileBufferEncrypted);
                    toServer.flush();

                    // count and print the packet in string
                    packetCount++;
                    // System.out.println("packetCount:" + packetCount);
                    // System.out.println(Base64.getEncoder().encodeToString(fromFileBuffer));
                    // System.out.println(new String(fromFileBuffer));
                    // System.out.println(new String(fromFileBufferEncrypted));
                    // System.out.println();

                }

                System.out.println("Sent " + args[i]);
                System.out.println("Total packets sent: " + packetCount);
                System.out.println("");

                if (i == args.length - 1) {
                    // send EOF packet
                    toServer.writeInt(Packet.END); // 4 => End of transfer
                    bufferedFileInputStream.close();
                    fileInputStream.close();
                }
            }

            System.out.println("Closing connection...");

        } catch (Exception e) {
            e.printStackTrace();
        }

		long timeTaken = System.nanoTime() - timeStarted;
		System.out.println("Program took: " + timeTaken/1000000.0 + "ms to run");
    }

    public static void sendMessage(String message, int num) throws IOException {
        toServer.writeUTF(message);
        toServer.writeInt(num);
    }

    public static void sendMessage(byte[] message, int num) throws IOException {
        toServer.writeInt(message.length);
        toServer.write(message);
        toServer.flush();
        toServer.writeInt(num);
    }


    public static String receiveMessage(int num) throws IOException {
        toServer.writeInt(num);
        return fromServer.readUTF();
    }

    public static String receiveMessage() throws IOException {
        return fromServer.readUTF();
    }
}
