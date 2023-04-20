package Asteroids2;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class Bullet extends Character {
	private double startTime;
	private double totalDistance;
	public Bullet(double x, double y, Color color) {
		super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
		startTime = System.nanoTime();
		totalDistance = 0.0;
		getCharacter().setFill(color);
	
	}


	public double getDistance() {
		double speed = this.getSpeed();
		double time = 0.0167;
		double dist = speed*time;
		totalDistance += dist;
		return totalDistance;
	}
	
}