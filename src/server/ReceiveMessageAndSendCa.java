package src.server;

import src.io.IO;
import src.socket.SocketSender;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveMessageAndSendCa {

    private static int port = 4320;

    public static void main(String[] args) {
        if (args.length > 0) port = Integer.parseInt(args[0]);
//        establishConnection();

        String s = IO.fileReader("src/textfile/nonce_en_r.txt").toString();
        String str = IO.fileReader("src/textfile/nonce_en_r_c.txt").toString();

        if (s.equals(str)) {
            SocketSender socketSender = new SocketSender();

            if (args.length > 0) socketSender.setFilename(args[0]);
            String filename = socketSender.getFilename();

            if (args.length > 1) socketSender.setServerAddress(args[1]);
            String serverAddress = socketSender.getServerAddress();

            if (args.length > 2) socketSender.setPort(args[2]);
            int port = socketSender.getPort();

            socketSender.sendFile("CA.crt");
        }
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
                            newOut.append("_c");
                        }
                        newOut.append(out.charAt(i));
                    }

                    fileOutputStream = new FileOutputStream(String.valueOf(newOut));
                    bufferedFileOutputStream = new BufferedOutputStream(fileOutputStream);

                    // If the packet is for transferring a chunk of the file
                } else if (packetType == 1 && bufferedFileOutputStream != null) {

                    int numBytes = fromClient.readInt();
                    byte[] block = new byte[numBytes];
                    fromClient.readFully(block, 0, numBytes);


                    if (numBytes > 0) {
                        bufferedFileOutputStream.write(block, 0, numBytes);
                    }

                    if (numBytes < 117) {
                        System.out.println("Closing connection...");

                        bufferedFileOutputStream.close();
                        fileOutputStream.close();
                        fromClient.close();
                        toClient.close();
                        connectionSocket.close();
                    }
                }

            }
        } catch (Exception e) {e.printStackTrace();}
    }

}
