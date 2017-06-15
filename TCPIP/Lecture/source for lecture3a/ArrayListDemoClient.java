import java.io.*;
import java.net.*;
import java.util.*;

public class ArrayListDemoClient {
	// IO streams
	private ObjectInputStream fromServer;



	public ArrayListDemoClient(String serverHost, int serverPort) {
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket(serverHost, serverPort);

			// Create ObjectInputStream to receive object from server
			fromServer = new ObjectInputStream(socket.getInputStream());

			// Receive ArrayList of Line objects from server
			ArrayList<Line> lines = (ArrayList<Line>)fromServer.readObject();

			// Print output
			for(Line line : lines) {
				System.out.println(line.toString());
			}
		}
		catch(ClassNotFoundException ex) {
			System.err.println(ex);
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public static void main(String args[]) {
		if(args.length == 2) {
			/*
				args[0] => hostName
				args[1] => port
			*/
			ArrayListDemoClient client = new ArrayListDemoClient(args[0], Integer.parseInt(args[1]));
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}
