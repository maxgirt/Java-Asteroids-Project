package Asteroids1;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Bullet extends Character {
	private double startTime;
	public Bullet(double x, double y, Color color) {
		super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
		startTime = System.nanoTime();
		getCharacter().setFill(color);
	}


	public double getAge() {
		return System.nanoTime() - startTime;
	}
	
}
