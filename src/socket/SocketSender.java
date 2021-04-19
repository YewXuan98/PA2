package src.socket;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

public class SocketSender {

    private String filename  = "", serverAddress = "localhost";
    private int port = 4320;
    private long timeStarted;

    private DataOutputStream toServer;

    public void setFilename(String filename) {
        if (filename != null)
            this.filename = filename;
    }

    public void setServerAddress(String serverAddress) {
        if (serverAddress != null)
            this.serverAddress = serverAddress;
    }

    public void setPort(String port) {
        if (port != null)
            this.port = Integer.parseInt(port);
    }

    public String getFilename() {
        return filename;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public DataOutputStream establishConnection() {

        Socket clientSocket;

        DataInputStream fromServer;

        timeStarted = System.nanoTime();

        try {

            System.out.println("Establishing connection to server...");

            // Connect to server and get the input and output streams
            clientSocket = new Socket(serverAddress, port);
            toServer = new DataOutputStream(clientSocket.getOutputStream());
            fromServer = new DataInputStream(clientSocket.getInputStream());
            return toServer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendFile(String filename) {
        establishConnection();

        int numBytes;

        FileInputStream fileInputStream;
        BufferedInputStream bufferedFileInputStream;

        try {
            // Send the filename
            toServer.writeInt(0);
            toServer.writeInt(filename.getBytes().length);
            toServer.write(filename.getBytes());
            //toServer.flush();

            // Open the file
            fileInputStream = new FileInputStream(filename);
            bufferedFileInputStream = new BufferedInputStream(fileInputStream);

            byte[] fromFileBuffer = new byte[117];

            // Send the file
            for (boolean fileEnded = false; !fileEnded;) {
                numBytes = bufferedFileInputStream.read(fromFileBuffer);
                fileEnded = numBytes < 117;

                toServer.writeInt(1);
                toServer.writeInt(numBytes);
                toServer.write(fromFileBuffer);
                toServer.flush();
            }

            bufferedFileInputStream.close();
            fileInputStream.close();

            System.out.println("Closing connection...");

        } catch (Exception e) {e.printStackTrace();}

        long timeTaken = System.nanoTime() - timeStarted;
        System.out.println("Program took: " + timeTaken/1000000.0 + "ms to run");
    }


}
