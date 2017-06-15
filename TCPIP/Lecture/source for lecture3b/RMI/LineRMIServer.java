import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

public class LineRMIServer extends UnicastRemoteObject implements LineService {
	public LineRMIServer() throws RemoteException {
		super();
	}

	public Point getMidPoint(Point p1, Point p2) {
		Line line = new Line(p1, p2);
		return line.getMidPoint();
	}
	
	public static void main(String[] args) {
		try {
			Naming.rebind("LineService", new LineRMIServer());
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
	}
}