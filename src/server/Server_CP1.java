package src.server;

import src.constant.Packet;
import src.keys.CertReader;
import src.keys.PrivateKeyReader;
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

public class Server_CP1 {

	public static int port = 4321;
	public static final String serverAddress = "localhost";

	public static X509Certificate serverCert;
	public static PrivateKey serverPrivateKey;

	public static FileOutputStream fileOutputStream = null;
	public static BufferedOutputStream bufferedFileOutputStream = null;

	public static ServerSocket welcomeSocket;
	public static Socket connectionSocket;
	public static DataInputStream fromClient;
	public static DataOutputStream toClient;

	public static void verifyClient() throws Exception {

		//get server cert
		serverCert = CertReader.get("CA.crt");

		// read S private key
		serverPrivateKey = PrivateKeyReader.get("private_key.der");
		System.out.println("Server Private key: " + serverPrivateKey);

		welcomeSocket = new ServerSocket(port);
		connectionSocket = welcomeSocket.accept();
		fromClient = new DataInputStream(connectionSocket.getInputStream());
		toClient = new DataOutputStream(connectionSocket.getOutputStream());

		while (!connectionSocket.isClosed()) {
			int packetCount = 0;

			int packetType = fromClient.readInt();

			// If the packet is for transferring the filename
			if (packetType == Packet.AUTH) {

				System.out.println("Request for authentication...");

				String clientMessage = fromClient.readUTF();

				String encryptedClientMessage = Base64.getEncoder()
						.encodeToString(CipherGen.encryptCipher(serverPrivateKey, clientMessage.getBytes(), 0));

				toClient.writeUTF(encryptedClientMessage);

			} else if (packetType == Packet.C1) {
				toClient.writeUTF(Base64.getEncoder().encodeToString(serverCert.getEncoded()));
			} else if (packetType == Packet.ERROR) {
				System.out.println("Client closed connection due to failed verification");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		verifyClient();

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
				int packetCount = 0;

				int packetType = fromClient.readInt();

				// If the packet is for transferring the filename
				if (packetType == Packet.AUTH) {

					System.out.println("Request for authentication...");

					String clientMessage = fromClient.readUTF();

					String encryptedClientMessage = Base64.getEncoder()
							.encodeToString(CipherGen.encryptCipher(serverPrivateKey, clientMessage.getBytes(), 0));

					toClient.writeUTF(encryptedClientMessage);

				} else if (packetType == Packet.C1) {
					toClient.writeUTF(Base64.getEncoder().encodeToString(serverCert.getEncoded()));
				} else if (packetType == Packet.ERROR){
					System.out.println("Client closed connection due to failed verification");
				} else if (packetType == Packet.C2) {
					int numBytes = fromClient.readInt();

					byte[] filename = new byte[numBytes];
					// Must use read fully!
					// See: https://stackoverflow.com/questions/25897627/datainputstream-read-vs-datainputstream-readfully
					fromClient.readFully(filename, 0, numBytes);

					fileOutputStream = new FileOutputStream("recv_"+new String(filename, 0, numBytes));
					bufferedFileOutputStream = new BufferedOutputStream(fileOutputStream);

				// If the packet is for transferring a chunk of the file
				} else if (packetType == Packet.C3) {

					int numBytes = fromClient.readInt();
					int numBytesEncrypted = fromClient.readInt();
					byte[] block = new byte[numBytesEncrypted];
					fromClient.readFully(block, 0, numBytes);
					packetCount++;

					byte[] blockDecrypted = CipherGen.decryptCipher(serverPrivateKey, block, 0);

					if (numBytes > 0) {
						bufferedFileOutputStream.write(blockDecrypted, 0, numBytes);
					}

					if (numBytes < 117) {
						System.out.println("Closing connection...");
						System.out.println("Total packets received: " + packetCount);

						if (bufferedFileOutputStream != null) bufferedFileOutputStream.close();
						if (bufferedFileOutputStream != null) fileOutputStream.close();
						fromClient.close();
						toClient.close();
						connectionSocket.close();
					}
				} else if(packetType == Packet.END){
					System.out.println("Closing connection...");
					fromClient.close();
					toClient.close();
					connectionSocket.close();
				}

			}
		} catch (Exception e) {e.printStackTrace();}

	}

}
