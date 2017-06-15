import java.io.*;
import java.net.*;
import java.rmi.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MidPointCalculator extends JFrame {
	// LineService
	private static LineService lineService;

	// GUI Components
	private JTextField jtfPointOneX;
	private JTextField jtfPointOneY;
	private JTextField jtfPointTwoX;
	private JTextField jtfPointTwoY;
	private JTextField jtfMidPointX;
	private JTextField jtfMidPointY;
	private JButton jbtMidPoint;

	public MidPointCalculator() {
		// Display GUI
		displayFrame();
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
				try {
					Point p1 = new Point(Double.parseDouble(
						jtfPointOneX.getText()), Double.parseDouble(jtfPointOneY.getText()));
					Point p2 = new Point(Double.parseDouble(
						jtfPointTwoX.getText()), Double.parseDouble(jtfPointTwoY.getText()));

					// Invoke remote method
					Point midPoint = lineService.getMidPoint(p1, p2);

					// Display mid-point
					jtfMidPointX.setText(Double.toString(midPoint.getX()));
					jtfMidPointY.setText(Double.toString(midPoint.getY()));
				}
				catch(RemoteException ex) {
					System.out.println(ex);
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
		if(args.length == 1) {
			try {
				System.setSecurityManager(new RMISecurityManager());
				lineService = (LineService)Naming.lookup("//" + args[0] + "/LineService");
				new MidPointCalculator();
			}
			catch (Exception ex) {
				System.out.println(ex);
			}			
		}
		else {
			System.out.println("Invalid number of arguments!");
		}
	}
}
