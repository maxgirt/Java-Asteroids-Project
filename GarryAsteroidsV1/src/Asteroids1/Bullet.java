package Asteroids1;
import javafx.scene.shape.Polygon;

public class Bullet extends Character {
	private double startTime;
	public Bullet(double x, double y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
        startTime = System.nanoTime();
    }
	
	public double getAge() {
		return System.nanoTime() - startTime;
	}
	
}
