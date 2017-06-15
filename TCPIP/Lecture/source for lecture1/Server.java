import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	public Server() {
		try {
			// Create a ServerSocket
			ServerSocket serverSocket = new ServerSocket(8000);
			System.out.println("Server started at " + new Date() + "\n");

			while (true) {
				// Listen for a connection request
				Socket socket = serverSocket.accept();

				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
			
				// Receive radius from the client
				double radius = inputFromClient.readDouble();

				// Compute area
				double area = radius * radius * Math.PI;

				// Send area back to the client
				outputToClient.writeDouble(area);
				outputToClient.flush();
				System.out.println("Radius received from client: " + radius + "\n");
				System.out.println("Area found: " + area + "\n");
			}
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}

	public static void main(String[] args) {
		new Server();
	}
}
