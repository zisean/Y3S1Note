import java.io.*;
import java.net.*;
import java.util.*;

public class UDPGeometryServer {
	public final static int BUFFER_SIZE = 512;
	private byte[] buffer = new byte[BUFFER_SIZE];

	public UDPGeometryServer(int port) {
		try {
			// Create a DatagramSocket
			DatagramSocket socket = new DatagramSocket(port);
			System.out.println("Server started at " + new Date() + "\n");
			
			// Create DatagramPacket objects for request and response
			DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
			DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);

			while (true) {
				// Initialize buffer for each iteration
				Arrays.fill(buffer, (byte)0);

				// Receive request from client (2 Point objects)
				socket.receive(requestPacket);
				
				ByteArrayInputStream inputByteStream = new ByteArrayInputStream(buffer);
				ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(inputByteStream));

				Point p1 = (Point)is.readObject();
				Point p2 = (Point)is.readObject();

				Line line = new Line(p1, p2);

				is.close();

				// Display client number, hostname, IP address & value
				System.out.println("Client " + requestPacket.getAddress().getHostName() +
					" sent a packet " + new Date());
				System.out.println("p1: " + p1.toString());
				System.out.println("p2: " + p2.toString());

				// Send response to client (1 Point object)
				ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream(BUFFER_SIZE);
				ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(outputByteStream));
				os.flush();
				os.writeObject(line.getMidPoint());
				os.flush();
				os.close();

				responsePacket.setAddress(requestPacket.getAddress());
				responsePacket.setPort(requestPacket.getPort());
				responsePacket.setData(outputByteStream.toByteArray());
				socket.send(responsePacket);
			}
		}
		catch(ClassNotFoundException ex) {
			System.err.println(ex);
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
			new UDPGeometryServer(port);
		}
		else if(args.length == 1) {
			// if port is specified in parameter, use it.
			port = Integer.parseInt(args[0]);
			new UDPGeometryServer(port);
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}