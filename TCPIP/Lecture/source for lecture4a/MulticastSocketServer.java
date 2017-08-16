import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSocketServer {
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;

	public MulticastSocketServer() {
        try {
			// Open a new DatagramSocket, which will be used to send the data.
			DatagramSocket serverSocket = new DatagramSocket();

			// Get the address that we are going to connect to.
			InetAddress addr = InetAddress.getByName(INET_ADDR);

			for (int i = 0; true; i++) {
                String msg = "Sent message no " + i;

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                        msg.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);

                System.out.println("Server sent packet with msg: " + msg);
                Thread.sleep(500);
            }
        }
		catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
		catch (InterruptedException ex) {
            ex.printStackTrace();
        }
		catch (IOException ex) {
            ex.printStackTrace();
        }
	}

    public static void main(String[] args)  {
		new MulticastSocketServer();
    }
}