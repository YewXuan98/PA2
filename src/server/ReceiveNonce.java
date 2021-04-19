package src.server;

import src.io.IO;
import src.keys.ca.PrivateKeyReader;
import src.rsa.cipher.CipherGen;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;

/**
 * Receive nonce from client (step 1)
 */

public class ReceiveNonce {

	private static int port = 4320;

	public static void main(String[] args) throws Exception {
		if (args.length > 0) port = Integer.parseInt(args[0]);
		establishConnection();
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
							newOut.append("_r");
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

	public static void encryptNonce() throws Exception {

		// TODO: encrypt with server's private key (private_key.der)
		PrivateKey key = PrivateKeyReader.get("private_key.der");
		byte[] b = CipherGen.encryptCipher(key, IO.fileReader("src/textfile/nonce.txt").toString().getBytes());
		IO.fileWriter("src/textfile/nonce_r.txt", b.toString());
	}

}
