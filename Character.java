package Asteroids1;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {
	private Polygon character;
	private Point2D movement;
	public static int height = 400;
	public static int width = 600;

	public Character(Polygon polygon, double x, double y) {
		this.character = polygon;
		this.character.setTranslateX(x);
		this.character.setTranslateY(y);

		this.movement = new Point2D(0, 0);
	}

	public Polygon getCharacter() {
		return character;
	}

	public void turnLeft() {
		this.character.setRotate(this.character.getRotate() - 5);
	}

	public void turnRight() {
		this.character.setRotate(this.character.getRotate() + 5);
	}

	public void move() {
	    double x = this.character.getTranslateX();
	    double y = this.character.getTranslateY();
	    double asteroidWidth = this.character.getBoundsInLocal().getWidth();
	    double asteroidHeight = this.character.getBoundsInLocal().getHeight();
	    
	    x += this.movement.getX();
	    y += this.movement.getY();
	    
	    if (x < -asteroidWidth / 2) {
	        x += Asteroids.paneWidth + asteroidWidth;
	    } else if (x > Asteroids.paneWidth + asteroidWidth / 2) {
	        x -= Asteroids.paneWidth + asteroidWidth;
	    }
	    
	    if (y < -asteroidHeight / 2) {
	        y += Asteroids.paneHeight + asteroidHeight;
	    } else if (y > Asteroids.paneHeight + asteroidHeight / 2) {
	        y -= Asteroids.paneHeight + asteroidHeight;
	    }
	    
	    this.character.setTranslateX(x);
	    this.character.setTranslateY(y);
	}
	
	public void accelerate() {
		double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
		double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

		changeX *= 0.02;
		changeY *= 0.02;

		this.movement = this.movement.add(changeX, changeY);
	}
	
	public boolean collide(Character other) {
	    Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
	    return collisionArea.getBoundsInLocal().getWidth() != -1;
	}
	
	public Point2D getMovement() {
		return this.movement;
	}
	
	public void setMovement(Point2D movement) {
		this.movement = movement;
	}
	
	public double getSpeed() {
		return this.movement.magnitude();
	}
	
	public boolean isOppositeDirection() {
		Point2D direction = this.getMovement();
		double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX())) - this.getCharacter().getRotate();
		angle = Math.abs(angle);
	    return angle > 90;
	}

}


