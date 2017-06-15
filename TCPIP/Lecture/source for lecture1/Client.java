import java.io.*;
import java.net.*;

public class Client {
	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;

	public Client(double radius) {
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket("localhost", 8000);	
			// Socket socket = new Socket("192.168.1.123", 8000);
			
			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
			
			// Send the radius to the server
			toServer.writeDouble(radius);
			toServer.flush();
			
			// Get area from the server
			double area = fromServer.readDouble();
			
			// Display to the text area
			System.out.println("Area: " + area + "\n");
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	public static void main(String[] args) {
		// Get the radius from command line arguments
		if(args.length == 1)
			new Client(Double.parseDouble(args[0].trim()));
		else
			System.out.println("Invalid number of arguments");
	}
}
