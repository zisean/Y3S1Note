import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UDPGeometryClient extends JFrame {
	public final static int BUFFER_SIZE = 512;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private String serverHost;
	private int serverPort;
	private InetAddress serverAddress;
	private DatagramSocket socket;
	private DatagramPacket requestPacket;
	private DatagramPacket responsePacket;


	// GUI Components
	private JTextField jtfPointOneX;
	private JTextField jtfPointOneY;
	private JTextField jtfPointTwoX;
	private JTextField jtfPointTwoY;
	private JTextField jtfMidPointX;
	private JTextField jtfMidPointY;
	private JButton jbtMidPoint;

	public UDPGeometryClient(String serverHost, int serverPort) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;

		try {
			// Create a DatagramSocket
			socket = new DatagramSocket();

			// Get the InetAddress object for the server address
			serverAddress = InetAddress.getByName(this.serverHost);

			// Create DatagramPacket objects for request and response
			requestPacket =	new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);
			responsePacket = new DatagramPacket(buffer, buffer.length);

			// Display GUI
			displayFrame();
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}

	private void displayFrame() {
		// Set layout to GridLayout
		setLayout(new GridLayout(4, 3, 5, 5));

		// Instantiate & configure GUI Components
		jtfPointOneX = new JTextField();
		jtfPointOneY = new JTextField();
		jtfPointTwoX = new JTextField();
		jtfPointTwoY = new JTextField();

		jtfMidPointX = new JTextField();
		jtfMidPointX.setEditable(false);

		jtfMidPointY = new JTextField();
		jtfMidPointY.setEditable(false);

		jbtMidPoint = new JButton("Get Mid-point");

		// Add GUI components to frame
		add(new JLabel("Point P1 (x, y)"));
		add(jtfPointOneX);
		add(jtfPointOneY);
		add(new JLabel("Point P2 (x, y)"));
		add(jtfPointTwoX);
		add(jtfPointTwoY);
		add(new JLabel("Mid-point (x, y)"));
		add(jtfMidPointX);
		add(jtfMidPointY);
		add(new JLabel(""));
		add(jbtMidPoint);

		// Handle Event: Button clicked
		jbtMidPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point p1 = new Point(Double.parseDouble(
					jtfPointOneX.getText()), Double.parseDouble(jtfPointOneY.getText()));
				Point p2 = new Point(Double.parseDouble(
					jtfPointTwoX.getText()), Double.parseDouble(jtfPointTwoY.getText()));

				try {
					// Initialize buffer for each iteration
					Arrays.fill(buffer, (byte)0);

					// Send 2 Point objects to server
					ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream(BUFFER_SIZE);
					ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(outputByteStream));
					os.flush();
					os.writeObject(p1);
					os.writeObject(p2);
					os.flush();
					os.close();
					

					responsePacket.setAddress(requestPacket.getAddress());
					responsePacket.setPort(requestPacket.getPort());
					responsePacket.setData(outputByteStream.toByteArray());
					socket.send(responsePacket);

					// Receive Point object (mid-point) from server
					socket.receive(requestPacket);
					ByteArrayInputStream inputByteStream = new ByteArrayInputStream(buffer);
					ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(inputByteStream));
					
					Point midPoint = (Point)is.readObject();
					System.out.println(midPoint.toString());

					is.close();

					// Display mid-point
					jtfMidPointX.setText(Double.toString(midPoint.getX()));
					jtfMidPointY.setText(Double.toString(midPoint.getY()));

				}
				catch(ClassNotFoundException ex) {
					System.err.println(ex);
				}
				catch(IOException ex) {
					System.err.println(ex);
				}
			}
		});

		// Configure & show frame
		setTitle("Mid Point");
		setLocationRelativeTo(null); // Center the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(480, 180);
		setVisible(true);
	}

	public static void main(String args[]) {
		if(args.length == 2) {
			/*
				args[0] => hostName
				args[1] => port
			*/
			new UDPGeometryClient(args[0], Integer.parseInt(args[1]));
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}
