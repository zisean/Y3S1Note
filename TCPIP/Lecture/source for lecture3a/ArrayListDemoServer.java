import java.io.*;
import java.net.*;
import java.util.*;

public class ArrayListDemoServer {
	public ArrayListDemoServer(int port) {
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
			new ArrayListDemoServer(port);
		}
		else if(args.length == 1) {
			// if port is specified in parameter, use it.
			port = Integer.parseInt(args[0]);
			new ArrayListDemoServer(port);
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
				// Create ObjectOutputStream to write object to Client
				ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

				// Create an ArrayList of Line objects
				ArrayList<Line> lines = new ArrayList<Line>();
				
				// Create 5 Line objects from 2 Point objects each with
				// randomly generated coordinates.
				for(int i = 0; i < 5; i++) {
					
					// Generate 4 random numbers
					double[] random = new double[4];
					for(int j = 0; j < random.length; j++) {
						random[j] = (Math.random() * 200) - 100;
					}
					
					// Create Point objects and Line object
					Point p1 = new Point(random[0], random[1]);
					Point p2 = new Point(random[2], random[3]);
					Line line = new Line(p1, p2);
					
					// Add Line object to ArrayList
					lines.add(line);
				}

				// Write ArrayList of Line objects to client
				outputToClient.writeObject(lines);
				outputToClient.flush();
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