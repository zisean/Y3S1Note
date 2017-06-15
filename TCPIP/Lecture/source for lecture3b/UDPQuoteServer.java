import java.io.*;
import java.net.*;
import java.util.*;

public class UDPQuoteServer {
	public final static int BUFFER_SIZE = 256;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private String[] quotes;

	public UDPQuoteServer(int port) {
		// Initialize quotes
		initQuotes();

		try {
			// Create a DatagramSocket
			DatagramSocket socket = new DatagramSocket(port);
			System.out.println("Server started at " + new Date() + "\n");

			// Create DatagramPacket objects for request and response
			DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
			DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);

			while(true) {
				// Initialize buffer for each iteration
				Arrays.fill(buffer, (byte)0);

				// Receive request from client (random int)
				socket.receive(requestPacket);
				int randomInt = Integer.parseInt(new String(buffer).trim());

				// Display client number, hostname, IP address & value
				System.out.println("Client " + requestPacket.getAddress().getHostName() +
					" sent a packet " + new Date() +" with the value: " + randomInt + "\n");

				// Send quote as response to client
				responsePacket.setAddress(requestPacket.getAddress());
				responsePacket.setPort(requestPacket.getPort());
				responsePacket.setData(quotes[randomInt % quotes.length].getBytes());
				socket.send(responsePacket);
			}
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}

	private void initQuotes() {
		String[] quotes = {
			"Politics is applesauce. - Will Rogers",
			"Do not dwell in the past, do not dream of the future, concentrate the mind on the present moment. - Buddha",
			"Life is really simple, but we insist on making it complicated. -Confucius",
			"Perfection is not attainable, but if we chase perfection we can catch excellence. -Vince Lombardi"
		};
		this.quotes = quotes;
	}

	public static void main(String[] args) {
		int port;
		if(args.length == 0) {
			// if port is not specified in parameter, defaults to 8000
			port = 8000;
			new UDPQuoteServer(port);
		}
		else if(args.length == 1) {
			// if port is specified in parameter, use it.
			port = Integer.parseInt(args[0]);
			new UDPQuoteServer(port);
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}