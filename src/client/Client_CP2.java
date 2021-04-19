package src.client;

import src.constant.Packet;
import src.rsa.AesSolution;
import src.rsa.cipher.ByteGen;
import src.rsa.cipher.CipherGen;

import javax.crypto.SecretKey;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static src.client.Client_CP1.toServer;
import static src.client.Client_CP1.verify;

public class Client_CP2 {

    public static void main(String[] args) throws Exception {
        //TODO: get public key CA cert from server
        verify();
        SecretKey AESKey = AesSolution.generateSecretKey();
        if (AESKey != null) {
            String symKey = ByteGen.getBase64Format(AESKey.getEncoded());
            System.out.println("symKey is: " + symKey);

            //TODO: Share AES KEY
            toServer.writeInt(Packet.C2);
            System.out.println("Sending key to server");
            toServer.writeUTF(ByteGen.getBase64Format(AESKey.getEncoded()));

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
                    sendMessage(fileName.getBytes(), Packet.C2);

                    //TODO: Open the file
                    fileInputStream = new FileInputStream(args[i]);
                    bufferedFileInputStream = new BufferedInputStream(fileInputStream);

                    byte[] fromFileBuffer = new byte[117];

                    int packetCount = 200;
                    //TODO: send 3 packets

                    //TODO: numBytest = number of bytes before encryption, to be written
                    // Send the file
                    for (boolean fileEnded = false; !fileEnded; ) {
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
                                .encryptCipher(AESKey, fromFileBuffer, 1);

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
            System.out.println("Program took: " + timeTaken / 1000000.0 + "ms to run");
        }

    }

    public static void sendMessage(byte[] message, int num) throws IOException {
        toServer.writeInt(message.length);
        toServer.write(message);
        toServer.flush();
        toServer.writeInt(num);
    }
}
