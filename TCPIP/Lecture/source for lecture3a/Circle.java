import java.io.Serializable;

public class Circle implements Serializable {
	private double radius;
	
	public Circle(double radius) {
		this.radius = radius;
	}
	
	public Circle() {
		this(1.0);
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getCircumference() {
		return 2 * Math.PI * radius;
	}
	
	public double getArea() {
		return Math.PI * radius * radius;
	}
}