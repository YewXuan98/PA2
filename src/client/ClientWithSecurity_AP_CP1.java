package src.client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.List;

public class ClientWithSecurity_AP_CP1 {

    public static final String NONCE = "Hello World";
    public static final int port = 4321;
    public static final String serverAddress = "localhost";

    private static DataOutputStream toServer;
    private static DataInputStream fromServer;


    public static void main(String[] args) throws Exception {
        //TODO: get public key CA cert from server
        X509Certificate caCert = CertReader.get("CA.crt");

        PublicKey caPubKey = caCert.getPublicKey();

        System.out.println("CA Public Key: " + caPubKey + "\n");


        String filename = "100.txt";
//    	if (args.length > 0) filename = args[0];
//
//    	if (args.length > 1) filename = args[1];
//
//    	if (args.length > 2) port = Integer.parseInt(args[2]);

        int numBytes = 0;

        Socket clientSocket;


        FileInputStream fileInputStream;
        BufferedInputStream bufferedFileInputStream;

        long timeStarted = System.nanoTime();

        try {

            System.out.println("Establishing connection to server...");

            // Connect to server and get the input and output streams
            clientSocket = new Socket(serverAddress, port);
            toServer = new DataOutputStream(clientSocket.getOutputStream());
            fromServer = new DataInputStream(clientSocket.getInputStream());

			System.out.println("Sending file...");

//			// Send the filename
//			toServer.writeInt(0);
//			toServer.writeInt(filename.getBytes().length);
//			toServer.write(filename.getBytes());
//			//toServer.flush();
//
//			// Open the file
//			fileInputStream = new FileInputStream(filename);
//			bufferedFileInputStream = new BufferedInputStream(fileInputStream);
//
//	        byte [] fromFileBuffer = new byte[117];
//
//	        // Send the file
//	        for (boolean fileEnded = false; !fileEnded;) {
//				numBytes = bufferedFileInputStream.read(fromFileBuffer);
//				fileEnded = numBytes < 117;
//
//				toServer.writeInt(1);
//				toServer.writeInt(numBytes);
//				toServer.write(fromFileBuffer);
//				toServer.flush();
//			}
//
//	        bufferedFileInputStream.close();
//	        fileInputStream.close();
//
//			System.out.println("Closing connection...");
//
//
//		long timeTaken = System.nanoTime() - timeStarted;
//		System.out.println("Program took: " + timeTaken/1000000.0 + "ms to run");
    }

    public static void sendMessage(String message, int num) throws IOException {
        toServer.writeUTF(message);
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
