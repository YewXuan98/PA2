package src.client;

import src.nonce.NonceGen;
import src.server.ReceiveNonce;
import src.socket.SocketSender;

/**
 * Pass nonce to server (step 1)
 */

public class PassNonce {

	public static void main(String[] args) {
		//TODO: Generate a nonce on client side and send to server
		NonceGen.generateClientNonce();

		SocketSender socketSender = new SocketSender();

    	if (args.length > 0) socketSender.setFilename(args[0]);
		String filename = socketSender.getFilename();

		if (args.length > 1) socketSender.setServerAddress(args[1]);
		String serverAddress = socketSender.getServerAddress();

		if (args.length > 2) socketSender.setPort(args[2]);
		int port = socketSender.getPort();

		socketSender.sendFile(filename);
	}


}
