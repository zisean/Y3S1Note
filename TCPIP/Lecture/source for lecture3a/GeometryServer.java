import java.io.*;
import java.net.*;
import java.util.*;

public class GeometryServer {
	public GeometryServer(int port) {
		try {
			// Create a ServerSocket
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server started at " + new Date() + "\n");

			// Client number
			int clientNo = 1;

			while (true) {
				// Listen for a connection request
				Socket socket = serverSocket.accept();

				// Display client number, hostname & IP address
				InetAddress inetAddress = socket.getInetAddress();
				System.out.println("Client No. " + clientNo + " [" +
					inetAddress.getHostName() + "] [" + inetAddress.getHostAddress() +
					"] connected at " + new Date() + "\n");

				// Create & start new thread to handle this connection
				HandleClient clientThread = new HandleClient(socket);
				clientThread.start();

				// Increment client number
				clientNo++;
			}
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}

	public static void main(String[] args) {
		int port;
		if(args.length == 0) {
			// if port is not specified in parameter, defaults to 8000
			port = 8000;
			new GeometryServer(port);
		}
		else if(args.length == 1) {
			// if port is specified in parameter, use it.
			port = Integer.parseInt(args[0]);
			new GeometryServer(port);
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}

	// Inner class: Thread class for handling connection
	class HandleClient extends Thread {
		private Socket socket;

		public HandleClient(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				// Create ObjectInputStream to read object from Client
				ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());

				// Create ObjectOutputStream to write object to Client
				ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

				// Serve the client continuously
				while (true) {
					// Read Point objects from the client
					Point p1 = (Point)inputFromClient.readObject();
					Point p2 = (Point)inputFromClient.readObject();
					
					// Instantiate Line object
					Line line = new Line(p1, p2);
					
					// Get mid-point
					Point midPoint = line.getMidPoint();

					// Write Point object (mid-point of p1 & p2) to client
					outputToClient.writeObject(midPoint);
					outputToClient.flush();

					// Server Logging
					System.out.println("Point object 1 received: " + p1.toString());
					System.out.println("Point object 2 received: " + p2.toString());
					System.out.println("Line object instantiated: " + line.toString());
					System.out.println("Mid-point sent: " + midPoint.toString());
					System.out.println();
				}
			}
			catch(ClassNotFoundException ex) {
				System.err.println(ex);
			}
			catch(SocketException ex) {
				System.out.println("Client is disconnected.\n");
			}
			catch(IOException ex) {
				System.err.println(ex);
			}
		}
	}
}