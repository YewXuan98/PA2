package src.client;

import src.socket.SocketSender;

public class SendMessage {

    public static void main(String[] args) {

        SocketSender socketSender = new SocketSender();

        if (args.length > 0) socketSender.setFilename(args[0]);
            String filename = socketSender.getFilename();

        if (args.length > 1) socketSender.setServerAddress(args[1]);
            String serverAddress = socketSender.getServerAddress();

        if (args.length > 2) socketSender.setPort(args[2]);
            int port = socketSender.getPort();

        socketSender.sendFile("src/textfile/nonce_en_r.txt");
    }


}
