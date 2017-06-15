import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GeometryClient extends JFrame {
	// IO streams
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;

	// GUI Components
	private JTextField jtfPointOneX;
	private JTextField jtfPointOneY;
	private JTextField jtfPointTwoX;
	private JTextField jtfPointTwoY;
	private JTextField jtfMidPointX;
	private JTextField jtfMidPointY;
	private JButton jbtMidPoint;
	
	public GeometryClient(String serverHost, int serverPort) {
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket(serverHost, serverPort);

			// Create ObjectOutputStream to send object to the server
			toServer = new ObjectOutputStream(socket.getOutputStream());

			// Create ObjectInputStream to receive object from server
			fromServer = new ObjectInputStream(socket.getInputStream());

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
					// Send the Point objects to the server
					toServer.writeObject(p1);
					toServer.writeObject(p2);
					toServer.flush();

					// Receive Point object (mid-point) from server
					Point midPoint = (Point)fromServer.readObject();

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
			GeometryClient client = new GeometryClient(args[0], Integer.parseInt(args[1]));
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}
