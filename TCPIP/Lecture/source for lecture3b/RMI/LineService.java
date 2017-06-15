import java.rmi.*;

public interface LineService extends Remote {
	public Point getMidPoint(Point p1, Point p2) throws RemoteException;
}