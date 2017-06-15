import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIClient extends JFrame {
	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;

	// GUI Components
	private JTextField jtfRadius;
	private JTextField jtfCircumference;
	private JTextField jtfArea;
	private JButton jbtCalculate;

	public GUIClient(String serverHost, int serverPort) {
		try {
			// Create a socket to connect to the server
			Socket socket = new Socket(serverHost, serverPort);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());

			// Display GUI
			displayFrame();

		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	}

	private void displayFrame() {
		// Set layout to GridLayout
		setLayout(new GridLayout(4, 2, 5, 5));

		// Instantiate & configure GUI Components
		jtfRadius = new JTextField("0.0");
		jtfRadius.setHorizontalAlignment(SwingConstants.RIGHT);

		jtfCircumference = new JTextField("0.0");
		jtfCircumference.setEditable(false);
		jtfCircumference.setHorizontalAlignment(SwingConstants.RIGHT);

		jtfArea = new JTextField("0.0");
		jtfArea.setEditable(false);
		jtfArea.setHorizontalAlignment(SwingConstants.RIGHT);

		jbtCalculate = new JButton("Calculate");

		// Add GUI components to frame
		add(new JLabel("Radius"));
		add(jtfRadius);
		add(new JLabel("Circumference"));
		add(jtfCircumference);
		add(new JLabel("Area"));
		add(jtfArea);
		add(new JLabel(""));
		add(jbtCalculate);

		// Handle Event: Button clicked
		jbtCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double radius = Double.parseDouble(jtfRadius.getText().trim());

				try {
					// Send the radius to the server
					toServer.writeDouble(radius);
					toServer.flush();
					
					// Get circumference & area from the server
					double circumference = fromServer.readDouble();
					double area = fromServer.readDouble();
					
					// Display result
					jtfCircumference.setText(Double.toString(circumference));
					jtfArea.setText(Double.toString(area));
				}
				catch(IOException ex) {
					System.err.println(ex);
				}
			}
		});

		// Configure & show frame
		setTitle("Circle Calculator");
		setLocationRelativeTo(null); // Center the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(320, 160);
		setVisible(true);
	}

	public static void main(String args[]) {
		if(args.length == 2) {
			/*
				args[0] => hostName
				args[1] => port
			*/
			GUIClient client = new GUIClient(args[0], Integer.parseInt(args[1]));
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}
