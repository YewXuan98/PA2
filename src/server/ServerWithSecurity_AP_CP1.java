package src.server;

import src.constant.Packet;
import src.keys.ca.CertReader;
import src.keys.ca.PrivateKeyReader;
import src.rsa.cipher.CipherGen;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class ServerWithSecurity_AP_CP1 {

	public static int port = 4321;
	public static final String serverAddress = "localhost";

	private static final FileOutputStream fileOutputStream = null;
	private static final BufferedOutputStream bufferedFileOutputStream = null;


	public static void main(String[] args) {

    	if (args.length > 0) port = Integer.parseInt(args[0]);

		ServerSocket welcomeSocket;
		Socket connectionSocket;
		DataOutputStream toClient;
		DataInputStream fromClient;

		try {
			welcomeSocket = new ServerSocket(port);
			connectionSocket = welcomeSocket.accept();
			fromClient = new DataInputStream(connectionSocket.getInputStream());
			toClient = new DataOutputStream(connectionSocket.getOutputStream());

			while (!connectionSocket.isClosed()) {

				int packetType = fromClient.readInt();

				// If the packet is for transferring the filename
				if (packetType == Packet.AUTH) {

					System.out.println("Request for authentication...");

					String clientMessage = fromClient.readUTF();

					String encryptedClientMessage = Base64.getEncoder()
							.encodeToString(CipherGen.encryptCipher(serverPrivateKey,clientMessage.getBytes()));

					toClient.writeUTF(encryptedClientMessage);

				} else if (packetType == Packet.C1 && serverCert != null) {
					toClient.writeUTF(Base64.getEncoder().encodeToString(serverCert.getEncoded()));
				}
//				} if (packetType == 100) {
//					byte [] filename = new byte[numBytes];
//					// Must use read fully!
//					// See: https://stackoverflow.com/questions/25897627/datainputstream-read-vs-datainputstream-readfully
//					fromClient.readFully(filename, 0, numBytes);
//
//					fileOutputStream = new FileOutputStream("recv_"+new String(filename, 0, numBytes));
//					bufferedFileOutputStream = new BufferedOutputStream(fileOutputStream);
//
//				// If the packet is for transferring a chunk of the file
//				} else if (packetType == Packet.EB) {
//
//					int numBytes = fromClient.readInt();
//					byte [] block = new byte[numBytes];
//					fromClient.readFully(block, 0, numBytes);
//
//					if (numBytes > 0)
//						bufferedFileOutputStream.write(block, 0, numBytes);
//
//					if (numBytes < 117) {
//						System.out.println("Closing connection...");
//
//						if (bufferedFileOutputStream != null) bufferedFileOutputStream.close();
//						if (bufferedFileOutputStream != null) fileOutputStream.close();
//						fromClient.close();
//						toClient.close();
//						connectionSocket.close();
//					}
//				}
//				else if (packetType == Packet.ERROR){
//					System.out.println("Client closed connection due to failed verification");
//				}
//
			}
		} catch (Exception e) {e.printStackTrace();}

	}

}
