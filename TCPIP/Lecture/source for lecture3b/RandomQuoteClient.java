import java.io.*;
import java.net.*;
import java.util.*;

public class RandomQuoteClient {
	public final static int BUFFER_SIZE = 256;
	private byte[] buffer = new byte[BUFFER_SIZE];

	public RandomQuoteClient(String serverHost, int serverPort) {
		try {
			// Initialize buffer for each iteration
			Arrays.fill(buffer, (byte)0);
		
			// Create a DatagramSocket
			DatagramSocket socket = new DatagramSocket();
			
			// Get the InetAddress object for the server address
			InetAddress serverAddress = InetAddress.getByName(serverHost);

			// Create DatagramPacket objects for request and response
			DatagramPacket requestPacket =
				new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
			DatagramPacket responsePacket =
				new DatagramPacket(buffer, buffer.length);
				
			// Send a random int to server
			Random random = new Random();
			int randomInt = Math.abs(random.nextInt());	// Remove negative sign
			requestPacket.setData(Integer.toString(randomInt).getBytes());
			socket.send(requestPacket);
			
			// Receive quote from server
			socket.receive(responsePacket);
			String quote = new String(buffer).trim();
			
			// Display quote
			System.out.println(quote);
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}

	public static void main(String[] args) {
		if(args.length == 2) {
			/*
				args[0] => hostName
				args[1] => port
			*/
			new RandomQuoteClient(args[0], Integer.parseInt(args[1]));
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}