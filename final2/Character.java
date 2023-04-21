package Asteroids2;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {
	protected Polygon character;
	private Point2D movement;

	public Point2D position;
	private static final double ACCELERATION = 0.05;

	public Character(Polygon polygon, double x, double y) {
		this.character = polygon;
		this.character.setTranslateX(x);
		this.character.setTranslateY(y);
		this.position = new Point2D(x, y);

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
	        x += Asteroids.PANE_WIDTH + asteroidWidth;
	    } else if (x > Asteroids.PANE_WIDTH + asteroidWidth / 2) {
	        x -= Asteroids.PANE_WIDTH + asteroidWidth;
	    }
	    
	    if (y < -asteroidHeight / 2) {
	        y += Asteroids.PANE_HEIGHT + asteroidHeight;
	    } else if (y > Asteroids.PANE_HEIGHT + asteroidHeight / 2) {
	        y -= Asteroids.PANE_HEIGHT + asteroidHeight;
	    }
	    
	    this.character.setTranslateX(x);
	    this.character.setTranslateY(y);
	}
	
	public void accelerate() {
		double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
		double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

		changeX *= ACCELERATION;
		changeY *= ACCELERATION;

		this.movement = this.movement.add(changeX, changeY);
	}
	
	public boolean collide(Character other) {
	    Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
	    return collisionArea.getBoundsInLocal().getWidth() != -1;
	}
	
	public Point2D getMovement() {
		return this.movement;
	}
	
	public Point2D getPosition(){
		return this.position;
	}
	public void setPosition(Point2D position){
		this.position = position;
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
	
	public void resetPosition(double x, double y) {
	    this.position = new Point2D(x, y);
	    this.character.setTranslateX(x);
	    this.character.setTranslateY(y);
	}

}