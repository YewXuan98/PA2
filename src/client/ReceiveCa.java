package src.client;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

public class ReceiveCa {

    private static int port = 4320;

    public static void main(String[] args) {
        if (args.length > 0) port = Integer.parseInt(args[0]);
        establishConnection();
        PublicKey caKey = Client.getPublicKey(); //public key from CA
        System.out.println(caKey);
    }

    public static void establishConnection() {
        ServerSocket welcomeSocket;
        Socket connectionSocket;
        DataOutputStream toClient;
        DataInputStream fromClient;

        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedFileOutputStream = null;

        try {
            welcomeSocket = new ServerSocket(port);
            connectionSocket = welcomeSocket.accept();
            fromClient = new DataInputStream(connectionSocket.getInputStream());
            toClient = new DataOutputStream(connectionSocket.getOutputStream());

            while (!connectionSocket.isClosed()) {

                int packetType = fromClient.readInt();

                // If the packet is for transferring the filename
                if (packetType == 0) {

                    System.out.println("Receiving file...");

                    int numBytes = fromClient.readInt();
                    byte[] filename = new byte[numBytes];
                    // Must use read fully!
                    // See: https://stackoverflow.com/questions/25897627/datainputstream-read-vs-datainputstream-readfully
                    fromClient.readFully(filename, 0, numBytes);

                    StringBuilder newOut = new StringBuilder();
                    String out = new String(filename, 0, numBytes);
                    for (int i = 0; i < out.length(); i++) {
                        if (out.charAt(i) == '.') {
                            newOut.append("_ca");
                        }
                        newOut.append(out.charAt(i));
                    }

                    fileOutputStream = new FileOutputStream(String.valueOf(newOut));
                    bufferedFileOutputStream = new BufferedOutputStream(fileOutputStream);

                    // If the packet is for transferring a chunk of the file
                } else if (packetType == 1) {

                    int numBytes = fromClient.readInt();
                    byte[] block = new byte[numBytes];
                    fromClient.readFully(block, 0, numBytes);

                    if (numBytes > 0) {
                        bufferedFileOutputStream.write(block, 0, numBytes);
                    }

                    if (numBytes < 117) {
                        System.out.println("Closing connection...");

                        if (bufferedFileOutputStream != null) bufferedFileOutputStream.close();
                        if (bufferedFileOutputStream != null) fileOutputStream.close();
                        fromClient.close();
                        toClient.close();
                        connectionSocket.close();
                    }
                }

            }
        } catch (Exception e) {e.printStackTrace();}
    }



}
