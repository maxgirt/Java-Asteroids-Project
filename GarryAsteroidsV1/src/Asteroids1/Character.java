package Asteroids1;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

//why is this an abstract class?
//most of our instance variables should be declared private, why are height and width set to public?
//why are setters void  --> not returning anything, just updating variable?
//we should make our height and width static and final since they will not be changed --> they should remain constant 
//final variables should be named in all CAPS
//maybe we should have a constants class for all our final variables that will not be changed
public abstract class Character {
	private Polygon character;
	private Point2D movement;
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public Point2D position;

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

		changeX *= 0.05;
		changeY *= 0.05;

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